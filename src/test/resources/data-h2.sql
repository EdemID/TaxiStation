-- insert clients
INSERT INTO clients(name, order_number)
VALUES ('Danil-client', 'No order');
-- insert dispatchers
INSERT INTO dispatchers(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('Germany-dispatcher','Sunday', '19:00', '21:00', true);

INSERT INTO dispatchers(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('Mikhail-dispatcher','Sunday', '13:00', '23:00', true);

-- insert drivers
INSERT INTO drivers(name, dayoff, car, workStatus, busy)
VALUES ('Nikola-driver', 'Sunday', 'едет на машине', true, true);

INSERT INTO drivers(name, dayoff, car, workStatus, busy)
VALUES ('Aurora-driver', 'Sunday', 'free', true, false );

-- insert cars
INSERT INTO cars(number_car, resource, busy)
VALUES ('random-car', 5, true);

INSERT INTO cars(number_car, resource, busy)
VALUES ('good luck-car', 1, false);

INSERT INTO cars(number_car, resource, busy)
VALUES ('armagedon-car', 0, false);

-- insert mechanic
INSERT INTO service(repair_time, resource)
VALUES (20000, 5);