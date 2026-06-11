import { useEffect, useState } from 'react'
import type { ComponentProps } from 'react'
import './App.css'
import {
  createTask,
  deleteTask,
  listTasks,
  updateTask,
} from './api/tasksApi'
import { taskStatusLabels } from './types/task'
import type { Task, TaskStatus } from './types/task'

type FormSubmitEvent = Parameters<
  NonNullable<ComponentProps<'form'>['onSubmit']>
>[0]

const taskStatusOptions = Object.entries(taskStatusLabels) as Array<
  [TaskStatus, string]
>

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [draftStatusByTaskId, setDraftStatusByTaskId] = useState<
    Record<string, TaskStatus>
  >({})

  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')

  const [isLoading, setIsLoading] = useState(false)
  const [isCreating, setIsCreating] = useState(false)
  const [savingTaskId, setSavingTaskId] = useState<string | null>(null)
  const [deletingTaskId, setDeletingTaskId] = useState<string | null>(null)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)

  async function loadTasks() {
    try {
      setIsLoading(true)
      setErrorMessage(null)

      const tasksFromApi = await listTasks()

      setTasks(tasksFromApi)
      setDraftStatusByTaskId(createDraftStatusMap(tasksFromApi))
    } catch {
      setErrorMessage('Não foi possível carregar as tarefas.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadTasks()
  }, [])

  async function handleCreateTask(event: FormSubmitEvent) {
    event.preventDefault()

    if (!title.trim()) {
      setErrorMessage('Informe um título para a tarefa.')
      return
    }

    try {
      setIsCreating(true)
      setErrorMessage(null)

      const createdTask = await createTask({
        title,
        description,
      })

      setTasks((currentTasks) => [createdTask, ...currentTasks])

      setDraftStatusByTaskId((currentDrafts) => ({
        ...currentDrafts,
        [createdTask.id]: createdTask.status,
      }))

      setTitle('')
      setDescription('')
    } catch {
      setErrorMessage('Não foi possível criar a tarefa.')
    } finally {
      setIsCreating(false)
    }
  }

  function handleSelectStatus(taskId: string, status: TaskStatus) {
    /*
     * Aqui NÃO chamamos a API.
     * Apenas guardamos o novo status escolhido localmente.
     */
    setDraftStatusByTaskId((currentDrafts) => ({
      ...currentDrafts,
      [taskId]: status,
    }))
  }

  async function handleSaveStatus(task: Task) {
    const draftStatus = draftStatusByTaskId[task.id] ?? task.status

    if (draftStatus === task.status) {
      return
    }

    try {
      setSavingTaskId(task.id)
      setErrorMessage(null)

      const updatedTask = await updateTask(task.id, {
        title: task.title,
        description: task.description ?? '',
        status: draftStatus,
      })

      setTasks((currentTasks) =>
        currentTasks.map((currentTask) =>
          currentTask.id === updatedTask.id ? updatedTask : currentTask,
        ),
      )

      setDraftStatusByTaskId((currentDrafts) => ({
        ...currentDrafts,
        [updatedTask.id]: updatedTask.status,
      }))
    } catch {
      setErrorMessage('Não foi possível salvar o novo status.')
    } finally {
      setSavingTaskId(null)
    }
  }

  async function handleDeleteTask(id: string) {
    try {
      setDeletingTaskId(id)
      setErrorMessage(null)

      await deleteTask(id)

      setTasks((currentTasks) =>
        currentTasks.filter((task) => task.id !== id),
      )

      setDraftStatusByTaskId((currentDrafts) => {
        const nextDrafts = { ...currentDrafts }
        delete nextDrafts[id]
        return nextDrafts
      })
    } catch {
      setErrorMessage('Não foi possível remover a tarefa.')
    } finally {
      setDeletingTaskId(null)
    }
  }

  const totalTasks = tasks.length
  const completedTasks = tasks.filter((task) => task.status === 'DONE').length
  const pendingChangesCount = tasks.filter((task) => {
    const draftStatus = draftStatusByTaskId[task.id] ?? task.status
    return draftStatus !== task.status
  }).length

  return (
    <main className="page">
      <section className="container">
        <header className="hero">
          <div>
            <p className="eyebrow">Desafio Técnico Inter</p>
            <h1>Gerenciador de Tarefas</h1>
            <p className="description">
              API Java/Spring Boot integrada com React + TypeScript.
            </p>
          </div>

          <div className="summary-grid">
            <div className="summary-card">
              <span>Total</span>
              <strong>{totalTasks}</strong>
            </div>

            <div className="summary-card">
              <span>Concluídas</span>
              <strong>{completedTasks}</strong>
            </div>

            <div className="summary-card pending">
              <span>Pendentes de salvar</span>
              <strong>{pendingChangesCount}</strong>
            </div>
          </div>
        </header>

        <form className="task-form" onSubmit={handleCreateTask}>
          <div className="form-header">
            <div>
              <h2>Nova tarefa</h2>
              <p>Cadastre uma tarefa para acompanhar seu progresso.</p>
            </div>
          </div>

          <div className="field">
            <label htmlFor="title">Título</label>
            <input
              id="title"
              type="text"
              placeholder="Ex: OPERAÇÃO"
              value={title}
              onChange={(event) => setTitle(event.target.value)}
            />
          </div>

          <div className="field">
            <label htmlFor="description">Descrição</label>
            <textarea
              id="description"
              placeholder="Detalhe a tarefa"
              value={description}
              onChange={(event) => setDescription(event.target.value)}
            />
          </div>

          <button type="submit" disabled={isCreating}>
            {isCreating ? 'Criando...' : 'Criar tarefa'}
          </button>
        </form>

        {errorMessage && (
          <div className="error-message">
            {errorMessage}
          </div>
        )}

        <section className="tasks-section">
          <div className="tasks-header">
            <div>
              <h2>Tarefas</h2>
              <p>Altere o status e clique em salvar para persistir no banco.</p>
            </div>

            <button
              type="button"
              className="secondary-button"
              onClick={loadTasks}
              disabled={isLoading}
            >
              {isLoading ? 'Atualizando...' : 'Atualizar lista'}
            </button>
          </div>

          {isLoading && tasks.length === 0 ? (
            <p className="empty-state">Carregando tarefas...</p>
          ) : null}

          {!isLoading && tasks.length === 0 ? (
            <p className="empty-state">Nenhuma tarefa cadastrada.</p>
          ) : null}

          <div className="task-list">
            {tasks.map((task) => {
              const draftStatus = draftStatusByTaskId[task.id] ?? task.status
              const hasPendingStatusChange = draftStatus !== task.status
              const isSavingThisTask = savingTaskId === task.id
              const isDeletingThisTask = deletingTaskId === task.id

              return (
                <article className="task-card" key={task.id}>
                  <div className="task-content">
                    <div className="task-title-row">
                      <h3>{task.title}</h3>

                      <span className={`status-pill ${draftStatus.toLowerCase()}`}>
                        {taskStatusLabels[draftStatus]}
                      </span>
                    </div>

                    <p>{task.description || 'Sem descrição'}</p>

                    <small>
                      Criada em: {new Date(task.createdAt).toLocaleString('pt-BR')}
                    </small>

                    {hasPendingStatusChange && (
                      <div className="pending-change">
                        Status alterado localmente. Clique em salvar para atualizar o banco.
                      </div>
                    )}
                  </div>

                  <div className="task-actions">
                    <label>
                      Status
                      <select
                        value={draftStatus}
                        onChange={(event) =>
                          handleSelectStatus(
                            task.id,
                            event.target.value as TaskStatus,
                          )
                        }
                        disabled={isSavingThisTask || isDeletingThisTask}
                      >
                        {taskStatusOptions.map(([status, label]) => (
                          <option key={status} value={status}>
                            {label}
                          </option>
                        ))}
                      </select>
                    </label>

                    {hasPendingStatusChange && (
                      <button
                        type="button"
                        className="save-button"
                        onClick={() => handleSaveStatus(task)}
                        disabled={isSavingThisTask}
                      >
                        {isSavingThisTask ? 'Salvando...' : 'Salvar status'}
                      </button>
                    )}

                    <button
                      type="button"
                      className="danger-button"
                      onClick={() => handleDeleteTask(task.id)}
                      disabled={isDeletingThisTask}
                    >
                      {isDeletingThisTask ? 'Removendo...' : 'Remover'}
                    </button>
                  </div>
                </article>
              )
            })}
          </div>
        </section>
      </section>
    </main>
  )
}

function createDraftStatusMap(tasks: Task[]): Record<string, TaskStatus> {
  return tasks.reduce<Record<string, TaskStatus>>((drafts, task) => {
    drafts[task.id] = task.status
    return drafts
  }, {})
}

export default App