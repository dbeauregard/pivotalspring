-- spring.sql.init.platform=postgresql
DROP TABLE IF EXISTS houses;
CREATE TABLE houses (
    id BIGINT generated by default as identity,
    address VARCHAR(255),
    price INT,
    bdrm INT,
    bath INT,
    sqft INT,
    PRIMARY KEY (id)
);