import type { Task, TaskStatus } from '../types/task'

const API_BASE_URL = '/api/tasks'

interface CreateTaskPayload {
    title: string
    description: string
}

interface UpdateTaskPayload {
    title: string
    description: string
    status: TaskStatus
}

async function handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
        throw new Error('Erro ao comunicar com a API')
    }

    return response.json()
}

export async function listTasks(): Promise<Task[]> {
    const response = await fetch(API_BASE_URL)

    return handleResponse<Task[]>(response)
}

export async function createTask(payload: CreateTaskPayload): Promise<Task> {
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    })

    return handleResponse<Task>(response)
}

export async function updateTask(
    id: string,
    payload: UpdateTaskPayload,
): Promise<Task> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
    })

    return handleResponse<Task>(response)
}

export async function deleteTask(id: string): Promise<void> {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
    })

    if (!response.ok) {
        throw new Error('Erro ao remover tarefa')
    }
}