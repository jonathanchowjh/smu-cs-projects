

-- Create Table
CREATE TABLE IF NOT EXISTS `Bookings` (
  `booking_id` VARCHAR(255) NOT NULL,
  `destination_id` VARCHAR(45) NOT NULL,
  `hotel_id` VARCHAR(45) NOT NULL,
  `hotel_room_id` VARCHAR(45) NOT NULL,
  `price` VARCHAR(45) NOT NULL,
  `start_date` VARCHAR(45) NOT NULL,
  `end_date` VARCHAR(45) NOT NULL,
  `cust_firstname` VARCHAR(45) NOT NULL,
  `cust_lastname` VARCHAR(45) NOT NULL,
  `cust_email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`booking_id`))




-- CREATE TABLE IF NOT EXISTS `Bookings` (
--   `booking_id` VARCHAR(255) NOT NULL,
--   `destination_id` VARCHAR(45) NOT NULL,
--   `hotel_id` VARCHAR(45) NOT NULL,
--   `hotel_room_id` VARCHAR(45) NOT NULL,
--   `price` VARCHAR(45) NOT NULL,
--   `start_date` VARCHAR(45) NOT NULL,
--   `end_date` VARCHAR(45) NOT NULL,
--   `cust_firstname` VARCHAR(45) NOT NULL,
--   `cust_lastname` VARCHAR(45) NOT NULL,
--   `cust_email` VARCHAR(45) NOT NULL,
--   PRIMARY KEY (`booking_id`))
