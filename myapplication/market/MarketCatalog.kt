package com.example.myapplication.market

import com.example.myapplication.wardrobe.ClothingCatalog

/** Stable ids are saved in the inventory. Change labels freely, but keep existing ids. */
data class MarketProduct(
    val id: String,
    val title: String,
    val price: Int,
    val description: String = "",
    val imageRes: Int? = ClothingCatalog.item(id)?.imageRes,
    val placeholderColor: Long = 0xFFE5EAF0,
)

data class MarketGroup(val id: String, val title: String, val products: List<MarketProduct>)
data class MarketCategory(val id: String, val title: String, val groups: List<MarketGroup>)
data class MarketDepartment(
    val id: String,
    val title: String,
    val symbol: String,
    val categories: List<MarketCategory>,
)

/** Edit the whole assortment here; the screen builds tabs, headings and cards from this data. */
object MarketCatalog {
    val departments = listOf(
        MarketDepartment("clothes", "Одежда", "👕", listOf(
            MarketCategory("trousers", "Брюки и шорты", listOf(
                MarketGroup("shorts", "Шорты", listOf(
                    MarketProduct("shorts_sand", "Синие шорты", 250, "Лёгкие шорты на каждый день.", placeholderColor = 0xFFE8D5AE),
                    MarketProduct("shorts_sport", "Спортивные шорты", 350, "Удобная модель для прогулок и спорта.", placeholderColor = 0xFFB8D6C1),
                )),
                MarketGroup("pants", "Брюки", listOf(
                    MarketProduct("pants_jeans", "Синие джинсы", 600, "Пока используется текстура шорт №1.", placeholderColor = 0xFFB9CFE8),
                    MarketProduct("pants_cargo", "Брюки карго", 800, "Пока используется текстура шорт №2.", placeholderColor = 0xFFCDD2B5),
                )),
            )),
            MarketCategory("outerwear", "Верхняя одежда", listOf(
                MarketGroup("jackets", "Рубашки и футболки", listOf(
                    MarketProduct("top_tshirt", "Полосатая футболка", 200, "Футболка из набора Clothes."),
                    MarketProduct("jacket_wind", "Футболка №2", 450, "Второй вариант из набора Clothes.", placeholderColor = 0xFFC1DBCF),
                    MarketProduct("jacket_denim", "Футболка №3", 750, "Третий вариант из набора Clothes.", placeholderColor = 0xFFB9CFE8),
                    MarketProduct("jacket_winter", "Футболка №5", 1200, "Пятый вариант из набора Clothes.", placeholderColor = 0xFFE4C5B4),
                )),
            )),
            MarketCategory("shoes", "Обувь", listOf(
                MarketGroup("footwear", "Обувь", listOf(
                    MarketProduct("shoes_canvas", "Кеды", 300, "Лёгкие повседневные кеды.", placeholderColor = 0xFFE8DCC8),
                    MarketProduct("shoes_running", "Кроссовки", 650, "Кроссовки для прогулок и тренировок.", placeholderColor = 0xFFBED8C7),
                    MarketProduct("shoes_boots", "Ботинки", 900, "Ботинки для прохладной погоды.", placeholderColor = 0xFFD4BDAB),
                )),
            )),
        )),
        MarketDepartment("home", "Дом", "⌂", listOf(
            MarketCategory("furniture", "Мебель", listOf(
                MarketGroup("furnishings", "Мебель", listOf(
                    MarketProduct("furniture_chair", "Рабочий стул", 500, "Стул для рабочего места.", placeholderColor = 0xFFD9C6B4),
                    MarketProduct("furniture_table", "Письменный стол", 1100, "Стол для учёбы и работы.", placeholderColor = 0xFFE6D5BA),
                    MarketProduct("furniture_sofa", "Компактный диван", 2200, "Диван для небольшой комнаты.", placeholderColor = 0xFFC3D2BC),
                )),
            )),
            MarketCategory("floor", "Пол", listOf(
                MarketGroup("flooring", "Напольные покрытия", listOf(
                    MarketProduct("floor_linoleum", "Светлый линолеум", 400, "Светлое покрытие для комнаты.", placeholderColor = 0xFFE4D9C6),
                    MarketProduct("floor_laminate", "Дубовый ламинат", 900, "Покрытие с древесным рисунком.", placeholderColor = 0xFFD2B48C),
                    MarketProduct("floor_parquet", "Тёмный паркет", 1500, "Паркет глубокого коричневого оттенка.", placeholderColor = 0xFFBFA18A),
                )),
            )),
            MarketCategory("walls", "Стены", listOf(
                MarketGroup("paint", "Покраска", listOf(
                    MarketProduct("paint_mint", "Мятная краска", 300, "Спокойный мятный цвет стен.", placeholderColor = 0xFFBBDDCB),
                    MarketProduct("paint_sand", "Песочная краска", 300, "Тёплый светлый цвет стен.", placeholderColor = 0xFFE6D5B3),
                )),
                MarketGroup("wallpaper", "Обои", listOf(
                    MarketProduct("wallpaper_stripe", "Обои в полоску", 550, "Светлые обои с полосатым рисунком.", placeholderColor = 0xFFCDD9E5),
                    MarketProduct("wallpaper_leaf", "Обои с листьями", 700, "Обои с растительным узором.", placeholderColor = 0xFFD1DABF),
                )),
            )),
        )),
        MarketDepartment("sport", "Спорт", "🚲", listOf(
            MarketCategory("bikes", "Велосипеды", listOf(
                MarketGroup("bicycles", "Велосипеды", listOf(
                    MarketProduct("bike_city", "Городской велосипед", 1800, "Велосипед для поездок по городу.", placeholderColor = 0xFFBBD7D0),
                    MarketProduct("bike_mountain", "Горный велосипед", 2800, "Велосипед для неровных дорожек.", placeholderColor = 0xFFE2C7B3),
                    MarketProduct("bike_bmx", "BMX", 3200, "Компактный велосипед для трюков.", placeholderColor = 0xFFCCD0E7),
                )),
            )),
            MarketCategory("scooters", "Самокаты", listOf(
                MarketGroup("kick_scooters", "Самокаты", listOf(
                    MarketProduct("scooter_fold", "Складной самокат", 700, "Лёгкий самокат для прогулок.", placeholderColor = 0xFFCCD9DD),
                    MarketProduct("scooter_stunt", "Трюковой самокат", 1400, "Самокат с прочной рамой.", placeholderColor = 0xFFE3CBB8),
                    MarketProduct("scooter_electric", "Электросамокат", 4500, "Самокат с электрическим приводом.", placeholderColor = 0xFFC8D2C8),
                )),
            )),
        )),
        MarketDepartment("electronics", "Техника", "▣", listOf(
            MarketCategory("phones", "Смартфоны", listOf(
                MarketGroup("smartphones", "Смартфоны", listOf(
                    MarketProduct("phone_basic", "Базовый смартфон", 1500, "Для связи и повседневных задач.", placeholderColor = 0xFFCCD9E6),
                    MarketProduct("phone_camera", "Смартфон с камерой", 2800, "Для фотографий и общения.", placeholderColor = 0xFFDDD0E8),
                    MarketProduct("phone_pro", "Мощный смартфон", 4200, "Для игр и требовательных приложений.", placeholderColor = 0xFFBFD5CE),
                )),
            )),
            MarketCategory("computers", "Компьютеры", listOf(
                MarketGroup("computing", "Компьютеры", listOf(
                    MarketProduct("computer_laptop", "Учебный ноутбук", 2500, "Ноутбук для занятий и домашних заданий.", placeholderColor = 0xFFDADFD9),
                    MarketProduct("computer_desktop", "Домашний компьютер", 4000, "Компьютер для работы и отдыха.", placeholderColor = 0xFFC9D6E2),
                    MarketProduct("computer_gaming", "Игровой компьютер", 6500, "Мощный компьютер для игр.", placeholderColor = 0xFFD6C9E2),
                )),
            )),
        )),
    )

    val products: List<MarketProduct> = departments.flatMap { department ->
        department.categories.flatMap { category -> category.groups.flatMap { it.products } }
    }

    fun product(id: String): MarketProduct? = products.firstOrNull { it.id == id }
}
