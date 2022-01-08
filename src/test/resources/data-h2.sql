-- insert client
INSERT INTO client(name, order_number)
VALUES ('Danil-client', 'No order');

INSERT INTO client(name, order_number)
VALUES ('Masha-client', 'No order');

-- insert dispatcher
INSERT INTO dispatcher(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('dispatcher-dayoff','dayoff', '00:00', '00:01', false);

INSERT INTO dispatcher(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('dispatcher-lunch','dayoff', '00:00', '00:59', true);

-- insert driver
INSERT INTO driver(name, dayoff, car, workStatus, busy)
VALUES ('driver-dayoff', 'dayoff', 'free', false, false );

INSERT INTO driver(name, dayoff, car, workStatus, busy)
VALUES ('driver-busy', 'dayoff', 'random car', true, true);

-- insert car
INSERT INTO car(number_car, resource, busy)
VALUES ('busy_car', 5, true);

-- insert mechanic
INSERT INTO service(repair_time, resource)
VALUES (20000, 5);