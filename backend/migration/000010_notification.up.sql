CREATE TABLE IF NOT EXISTS notification
(
    id         BIGSERIAL PRIMARY KEY,
    owner_id   BIGINT      NOT NULL REFERENCES "user" (id) ON DELETE CASCADE,
    issuer_id  BIGINT      REFERENCES "user" (id) ON DELETE SET NULL,
    feed_id    BIGINT      REFERENCES feed (id) ON DELETE CASCADE,
    comment_id BIGINT      REFERENCES comment (id) ON DELETE CASCADE,
    type       VARCHAR(64) NOT NULL,
    seen       BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notification_owner_id ON notification (owner_id);
CREATE INDEX IF NOT EXISTS idx_notification_created_at ON notification (created_at DESC);
