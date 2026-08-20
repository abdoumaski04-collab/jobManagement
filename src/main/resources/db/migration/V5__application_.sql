CREATE TABLE application(
    id BIGSERIAL primary key,
    condidat_id BIGINT not null references candidate_profiles(id),
    joboffer_id BIGINT not null references job_offers(id),
    status varchar(25) default 'PENDING' check ( status in ('PENDING','ACCEPTED','REJECTED','WITHDRAWN')),
    applied_at TIMESTAMP not null default now(),
    cvsnapshoturl varchar(150) not null,
    unique(condidat_id,joboffer_id)
);