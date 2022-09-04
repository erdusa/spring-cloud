CREATE TABLE tbl_regions (
   id INT NOT NULL,
   name VARCHAR(50) NOT NULL
);

CREATE TABLE tbl_customers (
   id INT NOT NULL,
   number_id VARCHAR(50) null ,
   first_name VARCHAR(50) ,
   last_name VARCHAR(50) ,
   email VARCHAR(50) ,
   photo_url VARCHAR(50) ,
   region_id int, state VARCHAR(50) 
);
