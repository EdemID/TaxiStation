-- insert dispatchers
INSERT INTO dispatchers(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('Germany','Sunday', '13:00', '21:00', true);

INSERT INTO dispatchers(name, dayoff, start_lunch, end_lunch, workstatus)
VALUES ('Mikhail','Sunday', '13:00', '23:00', true);

-- insert drivers
INSERT INTO drivers(name, dayoff, car, workStatus, busy)
VALUES ('Nikola', 'Day off', 'едет на машине', true, true);

INSERT INTO drivers(name, dayoff, car, workStatus, busy)
VALUES ('Aurora', 'Day off', 'free', true, false );

-- insert cars
INSERT INTO cars(number_car, resource, busy)
VALUES ('random', 5, true);

INSERT INTO cars(number_car, resource, busy)
VALUES ('good luck', 5, false);

INSERT INTO cars(number_car, resource, busy)
VALUES ('armagedon', 0, false);

-- insert mechanic
INSERT INTO service(repair_time, resource)
VALUES (20000, 5);