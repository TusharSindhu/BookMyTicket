CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_name VARCHAR(255) NOT NULL,
    seat_number VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    reserved_by_customer_id VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    -- A ticket for a specific seat at a specific event should be unique
    UNIQUE KEY uk_event_seat (event_name, seat_number)
);