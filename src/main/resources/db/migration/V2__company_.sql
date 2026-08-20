CREATE TABLE companies(
    id BIGSERIAL primary key,
    name varchar(100) not null,
    description varchar(255) not null,
    website varchar(255) not null,
    owner_id bigint not null references recruiters(id)
);