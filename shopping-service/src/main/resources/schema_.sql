CREATE TABLE tlb_invoices (
   number_invoice VARCHAR(50), description VARCHAR(20), customer_id int, create_at date, state VARCHAR(50)
);
CREATE TABLE tbl_invoce_items (
    invoice_id int, product_id int, quantity int, price float
);
