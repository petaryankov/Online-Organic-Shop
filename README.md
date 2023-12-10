# Online-Organic-Shop

# Table of Contents
- <a href="#about">About this Project</a>
- <a href="#design">Design</a>
- <a href="#functionalities">Functionalities</a>
- <a href="#project-structure">Project Structure</a>


# <p id="about">About this project</p>

OrganicShop is a web application that allows users to do online shopping by ordering products with delivery.<br>
After registration and login you can add some products to your cart and make an order.

# <p id="design">Design</p>
The design is based on <a href="https://bootstrapmade.com/">Bootstrap</a>.

# <p id="functionalities">Functionalities</p>
Before registration or login people are able to review the products and send a message with the contact form.

After registration, everyone automatically gets the "USER" role
and only the admin is able to change roles.

- <strong>Users</strong>
  - ADMIN, EMPLOYEE, USER
    - ADMIN 
      - Manually set up with access to all users along with their details.
      - Changing roles from "user" to "employee" or "employee" to "user".
      - View all orders history and finish or cancel orders.
      - Add, edit or delete products.
      - View its personal information, but cannot edit anything.
      - View sent messages and able to delete them.

    - EMPLOYEE
      - Manually set up employee are two - with usernames "employee1" and "employee2"
      - Only view the products.
      - View all orders history and finish or cancel orders.
      - View its personal information and edit names.
      - View sent messages but not able to delete them.
    - USER
      - Manually set up users are two - with usernames "user1" and "user2"
      - Add products to the cart
      - Remove products from the cart
      - Make orders
      - View its own orders, history and orders details
      - View its own cart
      - View its own personal information and edit names
      - View contact form and submit messages
     
      # <p id="project-structure">Project Structure</p>
- Server

  - **config** - files used to configure the application
  - **exceptions** - files containing custom exceptions
  - **model** - all database models used in the application
  - **repository** - files directly accessing mySQL database and manipulating data
  - **service** - all business logic used in the application
  - **validation** - files containing customs validation logic
  - **web** - controllers used to handle client requests
