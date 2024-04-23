CREATE
    TABLE
        IF NOT EXISTS product(
            id uuid NOT NULL,
            name VARCHAR NOT NULL,
            description VARCHAR,
            PRIMARY KEY(id)
        );

CREATE
    TABLE
        IF NOT EXISTS ratings(
            id uuid NOT NULL,
            product_id uuid NOT NULL,
            user_name VARCHAR NOT NULL,
            stars INT NOT NULL,
            comment VARCHAR,
            created TIMESTAMP,
            PRIMARY KEY(id)
        );

CREATE
    INDEX IF NOT EXISTS product_ratings ON
    ratings(product_id);
