package com.example.admin_server.Model;

public class Food {

        String Description,Image,MenuId,Name,Price;

        public Food(){

        }

        public Food(String description, String image, String menuId, String name, String price) {
            Description = description;
            Image = image;
            MenuId = menuId;
            Name = name;
            Price = price;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }

        public String getMenuId() {
            return MenuId;
        }

        public void setMenuId(String menuId) {
            MenuId = menuId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }
}
