CREATE TABLE IF NOT EXISTS services (
    id UUID primary key not null default uuid_generate_v4(),
    name character varying(100),
    data jsonb
);

