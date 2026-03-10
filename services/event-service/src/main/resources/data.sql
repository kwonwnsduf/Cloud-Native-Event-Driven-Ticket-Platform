INSERT INTO events (id, title, venue, event_date_time)
VALUES (1, 'IU Concert', 'Seoul Olympic Stadium', '2026-04-01 19:00:00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO events (id, title, venue, event_date_time)
VALUES (2, 'Coldplay Live', 'Busan Asiad Main Stadium', '2026-05-10 18:00:00')
ON CONFLICT (id) DO NOTHING;

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (1, 'A1', 'AVAILABLE', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (2, 'A2', 'AVAILABLE', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (3, 'A3', 'RESERVED', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (4, 'B1', 'AVAILABLE', 2)
ON CONFLICT (id) DO NOTHING;

INSERT INTO seats (id, seat_number, status, event_id)
VALUES (5, 'B2', 'SOLD', 2)
ON CONFLICT (id) DO NOTHING;