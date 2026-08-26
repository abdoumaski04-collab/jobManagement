CREATE TABLE candidate_skill (
                                 candidate_id BIGINT NOT NULL REFERENCES candidate_profiles(id) ON DELETE CASCADE,
                                 skill_id BIGINT NOT NULL REFERENCES skills(id) ON DELETE CASCADE,

                                 PRIMARY KEY (candidate_id, skill_id)
);