package com.example.myapplication.Dialogs

import com.example.myapplication.back.Game_progress
data class DialogResult(
    val unlocketTerms : Map<String, Boolean> = emptyMap(),
    val unlocketDialog: Map<String, Boolean> = emptyMap(),
    val unlocketQuest: Map<String, Boolean>,
    val complited : Boolean = false,
    val dialog_marker:Int
)
class father_dialogs(){
    fun dialog_get_termin():DialogResult{
        return  DialogResult(
            unlocketTerms = mapOf(),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf(),
            complited = true,
            dialog_marker = 2
        )
    }
}
class mother_dialogs(){
    fun dialog_going_production(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf(),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf(),
            complited = true,
            dialog_marker = 3
        )
    }
}
//Локации: 1 - Кухня , 2 - Гостинная ,3 - Банк , 4 - Парк , 5 - Склад , 6 - Магазин
//Персонажи: 0 - Описательные действия , 1 - наш персонаж , 2 - отец , 3 - мать , 4 - мысли персонажа ,5 - Кеша , 6 - Гера , 7 - Вова

class gera_dialogs(){
    fun dialog_1_interest_rate(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Процентная ставка" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое кредит?" to true,"Что такое процентная ставка?" to false),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_2_credit(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Кредит" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое сложный процент?" to true,"Что такое кредит?" to false ),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_6_tax_hard_interest(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Сложный процент" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое сложный процент?" to false ),
            complited = true,
            dialog_marker = 6
        )
    }

    fun dialog_3_tax_bank(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Банк" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое банк, и на чем он зарабатывает?" to false,"Что такое пассивный доход?" to true ),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_7_passiv_profit(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Пассивный доход" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое пассивный доход?" to false,"Что такое инфляция?" to true ),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_8_inflation(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("Инфляция" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое инфляция?" to false),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_5_tax_NDS(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("НДС" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое НДС?" to false, "Что такое НДФЛ?" to true),
            complited = true,
            dialog_marker = 6
        )
    }
    fun dialog_4_tax_NDFL(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf("НДФЛ" to true),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf("Что такое НДФЛ?" to false),
            complited = true,
            dialog_marker = 6
        )
    }

}
class kesha_dialogs(){
    fun dialog_gonka(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf(),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf(),
            complited = true,
            dialog_marker = 5
        )
    }
}
class vova_dialogs(){
    fun dialog_work(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf(),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf(),
            complited = true,
            dialog_marker = 7
        )
    }
}
class shop_dialogs(){
    fun dialog_shopping(): DialogResult{
        return DialogResult(
            unlocketTerms = mapOf(),
            unlocketQuest = mapOf(),
            unlocketDialog = mapOf(),
            complited = true,
            dialog_marker = 89
        )
    }
}
/*Функция обработчик результатов диалога , принимает в себя возвращаемое значение от функций - диалогов
 берет словарь unlocketTerms деструктуризирует каждый элемент словаря  до key value, далее проходимся по
 каждлому key и проверяем существует ли словарь с таким названием .containsKey(key) при нахождении
 просто изменяем значение на true по известному ключу */
fun processing_dialogResult(
    result: DialogResult,
    gameProgress: Game_progress
){

    result.unlocketTerms.forEach { key, value ->
        if (value && gameProgress.progress_terms.containsKey(key)){
            gameProgress.progress_terms[key] = true
        }
    }
    val dialogs = when (result.dialog_marker) {
        2 -> gameProgress.progress_dialogsFather_home
        3 -> gameProgress.progress_dialogsMother_home
        6 -> gameProgress.progress_dialogsGera_bank
        4 -> gameProgress.progress_dialogsElya_scholl
        else -> null
    }
    result.unlocketDialog.forEach { key, value ->
        val savedKey = if (key == "Что такое НДФЛ?" && dialogs?.containsKey(key) == false)
            "Что такое НДФЛ" else key
        if (dialogs?.containsKey(savedKey) == true) dialogs[savedKey] = value
    }
    result.unlocketQuest.forEach { key, value ->
        if (value && gameProgress.progress_quests.containsKey(key)){
            gameProgress.progress_quests[key] = true
        }
    }
}