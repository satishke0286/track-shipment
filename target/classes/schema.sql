Create table shipment_details (
id VARCHAR(50) PRIMARY KEY,
tracking_number VARCHAR(50) NOT NULL,
courier_code VARCHAR(5) NOT NULL,
origin VARCHAR(50) NOT NULL,
destination VARCHAR(50) NOT NULL
);