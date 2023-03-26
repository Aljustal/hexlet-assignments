CREATE TABLE people (
  id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
  first_name varchar(255),
  last_name varchar(255)
);

INSERT INTO
  people (first_name, last_name)
VALUES
  ('John', 'Smith'),
  ('Jack', 'Doe'),
  ('Jassica', 'Simpson'),
  ('Robert', 'Lock');
