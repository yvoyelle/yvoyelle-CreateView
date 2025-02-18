
        /**
         * problem1: Create a view called "firstname_lastname" in problem1.sql from the site_user table that only has the firstname and lastname columns.
         * NOTE: This table should NOT have the id and age.
         */

create view firstname_lastname AS
SELECT firstname, lastname 
FROM site_user;
