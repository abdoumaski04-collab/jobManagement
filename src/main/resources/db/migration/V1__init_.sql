CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT true,
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE recruiters (
                            id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE candidate_profiles (
                                    id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                                    bio TEXT,
                                    cv_url VARCHAR(500),
                                    phone VARCHAR(20)
);

CREATE TABLE admins (
                        id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE
);