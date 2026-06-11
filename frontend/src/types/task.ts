export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'DONE'

export interface Task {
    id: string
    title: string
    description: string | null
    status: TaskStatus
    createdAt: string
}

export const taskStatusLabels: Record<TaskStatus, string> = {
    PENDING: 'Pendente',
    IN_PROGRESS: 'Em andamento',
    DONE: 'Concluído',
}