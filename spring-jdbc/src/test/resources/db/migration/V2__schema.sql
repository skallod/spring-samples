CREATE TABLE IF NOT EXISTS instance (
    id UUID primary key not null default uuid_generate_v4(),
    name character varying(100),
    data jsonb
);

CREATE TABLE IF NOT EXISTS item (
    id UUID primary key not null default uuid_generate_v4(),
    name character varying(100),
    data jsonb,
    instance_id UUID,
    constraint instance_id_fk foreign key (instance_id) references instance(id)
);
