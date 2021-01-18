-- bootstrap restaurant data

INSERT INTO restaurants(name, address, contact)
VALUES
('Sala Thai', '3241 Walnut Ave, Fremont, CA 94538', '(510) 792-0770'),
('Olive Garden', '39145 Farwell Dr, Fremont, CA 94538', '(510) 796-7500'),
('Veg N Chaat Cuisine', '5168 Mowry Ave, Fremont, CA 94538', '(510) 362-7000'),
('Panda Express', '44047 Osgood Rd, Fremont, CA 94539', '(510) 490-1112'),
('Salsa Picante', '3933 Washington Blvd, Fremont, CA 94538', '(510) 656-8685');

-- Bootstrap User data - default password - test123

INSERT INTO users (name, username, password, contact, restaurant_id)
VALUES
('Sala Thai Owner','sthai','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1001),
('Olive Garden','olive_admin','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1002),
('VegNChat Manage','vegchat','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1003),
('Panda Express','express','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1004),
('Salsa Picante Owner','salsa','$2a$10$0mJRiKQV.AD6KhT4mb0Fi.cROXUD1gsOzhTEPXC8G/NFfAuML7RpK','210-358-098', 1005),
('Matt Customer','matts','$2a$10$J8.yy07rmL54Yuo7fXIxI.VBb6Swuj4dGvQiAEoFj02UxFpe6p1mm','455-456-8989', null),
('Robert Customer','robert','$2a$10$J8.yy07rmL54Yuo7fXIxI.VBb6Swuj4dGvQiAEoFj02UxFpe6p1mm','455-456-8989', null),
('Alice Customer','alice','$2a$10$J8.yy07rmL54Yuo7fXIxI.VBb6Swuj4dGvQiAEoFj02UxFpe6p1mm','455-456-8989', null),
('Karen Driver','karen','$2a$10$NbUzXUJVO2ahl4pvPpPkc.AEgK7ADKLtdoux/mFzAyObss2c60WG.','353-567-5656', null),
('Paul Driver','paully','$2a$10$NbUzXUJVO2ahl4pvPpPkc.AEgK7ADKLtdoux/mFzAyObss2c60WG.','353-567-5656', null),
('Jacob Driver','jacob','$2a$10$NbUzXUJVO2ahl4pvPpPkc.AEgK7ADKLtdoux/mFzAyObss2c60WG.','353-567-5656', null),
('Food Hub Admin','admin', '$2a$10$1eckdEA5ZNaIRlTxGhqu7u9azr4E5mMK0n3YXrX6lij6kAzl6yxj.','888-FOO-DHUB', null);

-- bootstrap roles data

INSERT INTO roles(user_id, role)
VALUES
(1,'ROLE_SHOP'),
(2,'ROLE_SHOP'),
(3,'ROLE_SHOP'),
(4,'ROLE_SHOP'),
(5,'ROLE_SHOP'),
(6,'ROLE_CUSTOMER'),
(7,'ROLE_CUSTOMER'),
(8,'ROLE_CUSTOMER'),
(9,'ROLE_DRIVER'),
(10,'ROLE_DRIVER'),
(11,'ROLE_DRIVER'),
(12,'ROLE_ADMIN');

-- bootstrap item data

INSERT INTO items(item_name, description, item_price, prep_time, restaurant_id, status)
VALUES
('Satay', 'Marinated chicken or been with thai spices and coconut milk then charboiled. served w/cucumber salad & peanut sauce.', 8.99, 12, 1001, 1),
('Tom Yum Gai', 'Spicy and sour soup thai style w/chicken, mushroom, onion, and tomato.', 8.99, 10, 1001, 1),
('Som Tum (papaya Salad)', 'Shredded green papaya, dried shrimp, tomato and topped w/ground peanut. mixed w/lime juice.', 9.99, 11, 1001, 1),
('Pad Kra Tiam', 'Sauteed meat w/garlic, onion, black pepper, and cilantro.', 10.99, 12, 1001, 1),
('Kao Pad (fried Rice)', 'Thai fried rice w/egg, onion, and choice of meat.', 11.99, 10, 1001, 1),
('Fried Banana With Ice Cream (vanilla)', 'Fried Banna served with couple of scoops of Vanilla ice cream.', 8.99, 7, 1001, 1),
('Chicken Marsala', 'Lightly floured grilled chicken breasts topped with savory mushroom and marsala wine sauce. Served with a side of fettuccine alfredo.', 19.49, 15, 1002, 1),
('Fettuccine Alfredo', 'Our chefs make it in house throughout the day with parmesan cheese, heavy cream, and garlic. Served with fettuccine pasta.', 15.99, 13, 1002, 1),
('Giant Cheese Stuffed Shells', 'Five shells filled with four-cheeses and topped with marinara, alfredo and toasted breadcrumbs.', 17.99, 12, 1002, 1),
('Lasagna Fritta', 'Fried parmesan-breaded lasagna, topped with parmesan cheese and meat sauce, with creamy alfredo.', 11.29, 10, 1002, 1),
('Toasted Ravioli', 'Ligtly fried ravioli filled with seasoned chicken. Served with marinara sauce.', 9.79, 10, 1002, 1),
('Veg Cheese Burger', 'Burger Served With Potato Fries And Choice Of Soda.', 5.99, 8, 1003, 1),
('Fresh Aloo Methi', 'Potatoes And Fresh Fenugreek Leaves Cooked With Herbs And Spices.', 9.99, 12, 1003, 1),
('Veg N Chaat Cuisine (Signature Dish)', 'Garden Vegetables, Coconut Milk, Curry Leaves', 11.99, 14, 1003, 1),
('Paneer Tikka Masala', 'Home Made Cheese Cooked With Ginger, Garlic, Spring Onion And Special Creamy Sauce', 9.99, 12, 1003, 1),
('Paneer Paratha', 'Pan Cooked Whole Wheat Bread Stuffed With Home Made Cheese, Onions And Special Mixtures Of Spices.', 4.99, 7, 1003, 1),
('Zunka Bhakar', 'Flat Breads Made From Millet Flour Served With Cooked Gram Flour Curry, Authentic Marathi Spices Served With Curd And Papadum.', 9.99, 15, 1003, 1),
('Kung Pao Chicken', 'A Szechwan-inspired dish with chicken, peanuts and vegetables, finished with chili peppers.', 9.99, 4, 1004, 1),
('SweetFire Chicken Breast', 'Crispy, white-meat chicken, red bell peppers, onions and pineapples in a bright and sweet chili sauce.', 9.99, 4, 1004, 1),
('Honey Walnut Shrimp', 'Large tempura-battered shrimp, wok-tossed in a honey sauce and topped with glazed walnuts.', 9.99, 5, 1004, 1),
('Vegetable Spring Roll', 'Cabbage, celery, carrots, green onions and Chinese noodles in a crispy wonton wrapper.', 4.99, 4, 1004, 1),
('Fried Rice', 'Prepared steamed white rice with soy sauce, eggs, peas, carrots and green onions.', 8.99, 7, 1004, 1),
('Chow Mein', 'Stir-fried wheat noodles with onions, celery and cabbage.', 8.99, 7, 1004, 1),
('Guacamole and Corn Tortillas', 'Fresh seasoned avocado and pico de gallo.', 9.50, 8, 1005, 1),
('Menudo Soup', 'Special red chilli pepper based soup with tripe, onion, cilantro and lime.', 12.00, 11, 1005, 1),
('Super Quesadilla', 'Monterey jack cheese, sour cream, guacamole and pico de gallo.', 12.85, 10, 1005, 1),
('Duo Combo Fajita', 'Sizzling hot plates, sauteed peppers, onions, Monterey Jack, guacamole, sour cream, Pico de Gallo, tortillas, Mexican rice and beans.', 22.0, 11, 1005, 1),
('California Good Taco', 'Pesca del diaz, onions, avocado, Monterey Jack cheese, tomatoes, shredded lettuce and tomatillo verde sauce.', 10.00, 12, 1005, 1),
('Rio Grande Burrito Gratine', 'Flour tortilla, sauteed onions, peppers in a guajillo sauce, refried beans, Monterey Jack cheese gratin, rose sauce and insalata.', 12.50, 10, 1005, 1);