INSERT INTO client(name, order_number)
VALUES ('Tom-client', 'No order');

INSERT INTO dispatcher(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('Vladimir-dispatcher-working','dayOff', '00:00', '00:01', true);

INSERT INTO driver(name, dayoff, car, workStatus, busy)
VALUES ('Aurora-driver-not-busy', 'dayOff', 'free', true, false);

INSERT INTO car(number_car, resource, busy)
VALUES ('car-not-busy', 5, false);