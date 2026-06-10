CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       title VARCHAR(120) NOT NULL,
                       description TEXT,
                       status VARCHAR(30) NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                       CONSTRAINT tasks_status_check CHECK (
                           status IN ('PENDING', 'IN_PROGRESS', 'DONE')
                           )
);