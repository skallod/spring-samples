CREATE TABLE IF NOT EXISTS region (
    id UUID primary key not null,
    created timestamp with time zone not null,
    edited timestamp with time zone not null,
    deleted timestamp with time zone,
    creator character varying(100),
    editor character varying(100),
    code character varying(100),
    name character varying(100),
    description text,
    data jsonb
);

CREATE TABLE IF NOT EXISTS availability_zone (
    id UUID primary key not null,
    created timestamp with time zone not null,
    edited timestamp with time zone not null,
    deleted timestamp with time zone,
    creator character varying(100),
    editor character varying(100),
    code character varying(100),
    name character varying(100),
    description text,
    data jsonb,
    region_id UUID,
    constraint region_id_fk foreign key (region_id) references region(id)
);

CREATE TABLE IF NOT EXISTS dcenter (
    id UUID primary key not null,
    created timestamp with time zone not null,
    edited timestamp with time zone not null,
    deleted timestamp with time zone,
    creator character varying(100),
    editor character varying(100),
    code character varying(100),
    name character varying(100),
    description text,
    data jsonb,
    availability_zone_id UUID,
    address text,
    constraint availability_zone_id_fk foreign key (availability_zone_id) references availability_zone(id)
);

