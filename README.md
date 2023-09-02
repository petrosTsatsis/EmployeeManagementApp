### Run a mysql database using docker

```bash
docker run --name employeesApp --network myApp -p 3309:3306 -e MYSQL_ROOT_PASSWORD=your_password -e MYSQL_DATABASE=Management -d mysql:8.0
```
After using the docker command and run the app to create the tables in the database you need to run the users.sql file in the assets directory to 
insert the default users in the database.

# Branches:
Existing Users and Roles in pre-configured initial sql

| USER   | PASSWORD | ROLES  |
|--------|----------|--------|
| user   | 12345678 | USER   |
| admin  | 12345678 | ADMIN  |
 

## Links:
* [install docker](https://tinyurl.com/2m3bhahn)
