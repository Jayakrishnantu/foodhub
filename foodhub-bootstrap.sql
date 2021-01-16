-- Bootstrap User data - default password - test123

INSERT INTO users (name, username, password, contact, restaurant_id)
VALUES
('Sala Thai Owner','sthai','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1000),
('Olive Garden','olive_admin','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1001),
('Spice Up','spiceup','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1002),
('Panda Express','express','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1003),
('End Customer','customer','$2a$10$J8.yy07rmL54Yuo7fXIxI.VBb6Swuj4dGvQiAEoFj02UxFpe6p1mm','455-456-8989', null),
('Delivery Driver','driver','$2a$10$NbUzXUJVO2ahl4pvPpPkc.AEgK7ADKLtdoux/mFzAyObss2c60WG.','353-567-5656', null),
('Administrator', 'admin', '$2a$10$1eckdEA5ZNaIRlTxGhqu7u9azr4E5mMK0n3YXrX6lij6kAzl6yxj.','888-FOO-DHUB', null);

-- bootstrap roles data

INSERT INTO roles(user_id, role)
VALUES
(1,'ROLE_SHOP'),
(2,'ROLE_SHOP'),
(3,'ROLE_SHOP'),
(4,'ROLE_SHOP'),
(5,'ROLE_CUSTOMER'),
(6,'ROLE_DRIVER'),
(7,'ROLE_ADMIN');

-- bootstrap restaurant data

INSERT INTO restaurants(name, address, contact)
VALUES
('Sala Thai', '200 Walnut Ave', '510-456-9090'),
('Olive Garden', '300 Mowrey Ave', '510-567-9090'),
('Spice Up', '2400 Stevenson Blvd', '456-898-0909'),
('Panda Express', '4100 Fremont Blvd', '510-678-5656');