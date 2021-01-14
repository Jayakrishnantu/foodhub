-- Bootstrap User data - default password - test123

INSERT INTO users (name, username, password)
VALUES
('Restaurant Owner','owner','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK'),
('End Customer','customer','$2a$10$J8.yy07rmL54Yuo7fXIxI.VBb6Swuj4dGvQiAEoFj02UxFpe6p1mm'),
('Delivery Driver','driver','$2a$10$NbUzXUJVO2ahl4pvPpPkc.AEgK7ADKLtdoux/mFzAyObss2c60WG.');

-- bootstrap roles data

INSERT INTO roles(user_id, role)
VALUES
(1,'ROLE_OWNER'),
(2,'ROLE_CUSTOMER'),
(3,'ROLE_DRIVER');