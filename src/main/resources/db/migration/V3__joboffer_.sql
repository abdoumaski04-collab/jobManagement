CREATE TABLE job_offers(
    id BIGSERIAL primary key ,
    title varchar(50) not null,
    description varchar(3000) not null,
    location varchar(255) not null,
    salary_range varchar(50) not null,
    company_id BIGINT not null references companies(id),
    status varchar(20) not null default 'OPEN' check ( status in ('OPEN','CLOSED')),
    created_at TIMESTAMP not null default now()
);
CREATE INDEX idx_job_offers_company_id ON job_offers(company_id);
CREATE INDEX idx_job_offers_status ON job_offers(status);