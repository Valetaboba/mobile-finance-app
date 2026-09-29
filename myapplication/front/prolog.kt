package com.example.myapplication.front

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myapplication.R
import com.example.myapplication.back.Game_progress


// каждая val перемнная - это диалог между скольк угодно людьми , но только в ОДНОЙ локации .
// number_location - четко определи какая цифра отвечает за какую локацию
// number_npc - четко пропиши какая цифра отвечает за какого персонажа
// Внутри функции DialogScene_pro
//Локации: 1 - Кухня , 2 - Гостинная ,3 - Банк , 4 - Парк , 5 - Склад , 6 - Магазин
//Персонажи: 0 - Описательные действия , 1 - наш персонаж , 2 - отец , 3 - мать , 4 - мысли персонажа ,5 - Кеша , 6 - Гера , 7 - Вова
data class DialogLine_location(
    val number_location: Int,
    val number_npc: Int,
    val text_npc: String
)
val dialogLines_father_morning = listOf(
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "Гостиная встретила ИМЯ прохладой и тиканьем старых настенных часов — они достались бабушке от её бабушки и переехали вместе с семьёй в новый дом. Папа сидел за письменным столом у окна, щурился от утреннего солнца и сосредоточенно читал что-то на экране компьютера – это были новости экономики. "),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "ИМЯ остановился в дверях. "),
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "Доброе утро, пап. Мама сказала, что ты хотел поговорить?"),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Доброе! Проходи, присаживайся. Есть серьёзный разговор."),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Тебе уже четырнадцать, так что мы с мамой решили: пора тебе оформить собственную дебетовую банковскую карту."),
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "Но у меня же уже есть карта. Зачем ещё одна? И что вообще значит «дебетовая»?"),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Дебетовая карта — это платёжный инструмент, который даёт доступ к деньгам на твоём банковском счёте. На ней лежат твои собственные деньги, поэтому в минус уйти нельзя: если на счёте ничего нет, оплатить не получится. Ни копейки — ни покупки. Просто и безопасно."),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Карта, которая у тебя сейчас — детская. Счёт, к которому она привязана, принадлежит мне. Я могу видеть все твои операции и при необходимости ставить ограничения — например, лимит на покупки в интернете."),
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "И что, у моей новой карты таких ограничений не будет?"),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Верно. Собственная карта — значит, ты сам решаешь, сколько потратить, а сколько отложить. Никто за тобой не следит. Но и никто тебя не подстрахует, если потратишь всё в первый день."),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "Папа сделал паузу и посмотрел на ИМЯ поверх монитора."),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Мы положим на новую карту небольшую сумму — к тем деньгам, что у тебя уже есть. Ты уже не ребёнок, пора разбираться с финансами самостоятельн…"),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "Папа не договорил — на телефон поступил звонок. По обрывкам разговора ИМЯ понял: звонят по важному вопросу."),
    DialogLine_location(number_location = 2,number_npc = 4, text_npc = "Ладно, пока позвоню Кеше — скажу, что в другой раз покатаемся."),
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "Кеш, мне тут с родителями по делам нужно. Давай в другой раз?"),
    DialogLine_location(number_location = 2,number_npc = 5, text_npc = "Да без проблем! Я в парке почти каждый день — в какое бы время ты ни пришёл, я наверняка там."),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "Папа как раз положил трубку."),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Придётся тебе пойти в банк одному — нам с мамой нужно срочно уехать по делам. Все подробности расскажет сотрудник банка. "),
    DialogLine_location(number_location = 2,number_npc = 5, text_npc = "Хорошо, без проблем."),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "В словарь добавлены новые определения: «Дебетовая карта», «Детская дебетовая карта»."),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "Для перемещения по сюжетным локациям нажмите на значок «мини-карта»"),
    DialogLine_location(number_location = 2,number_npc = 0, text_npc = "С помощью мини-карты вы можете перемещаться между игровыми локациями и узнавать много нового. Открывайте новые места — и новые возможности!")
)
// 1 - ИМЯ , 2 - Папа
val dialogLines_father_evening = listOf(
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "Пап, во время получения карты я встретил твоего друга Герасима и он сказал что я могу обратиться к нему за помощью по вопросам экономики"),
    DialogLine_location(number_location = 2,number_npc = 2, text_npc = "Отлично! В качестве мотивации к твоему обучению, я буду давать тебе по 100 монет за каждый тобой новый изученный термин. Начнем с завтрашнего дня."),
    DialogLine_location(number_location = 2,number_npc = 1, text_npc = "Хорошо, я тебя понял, буду стараться!"),
)
// 1 - ИМЯ , 3 - Мама , 0 - описательные действия
val dialogLines_mother_morning = listOf(
    DialogLine_location(number_location = 1,number_npc = 0, text_npc ="Кухня встретила ИМЯ тёплым запахом свежего кофе и поджаренного хлеба. За столом сидела мама и, нахмурив брови, сосредоточенно смотрела в экран телефона — видимо, что-то важное."),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc ="Доброе утро, мам…"),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc ="Доброе утро, ИМЯ. Чего это ты рановато для летних каникул?"),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc ="Меня Кеша разбудил. Зовёт в парк — на великах кататься."),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc ="Хорошо, только сначала загляни в холодильник, поешь как следует. Зная вас с Кешей — зависнете до вечера, а потом будешь голодный и злой бродить."),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc ="У ИМЯ есть шкала энергии. Ее хватает для посещения двух локаций в день."),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc = "Чтобы пополнить энергию и продолжить исследовать карту, нужно поесть — еда хранится в холодильнике на кухне." ),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc = "Выбранная вами еда влияет на уровень заполнения шкалы: вредная — мало, нейтральная — средне, здоровая — много." ),
    DialogLine_location(number_location = 2,number_npc = 4, text_npc = "ИМЯ достал из холодильника еду и быстро позавтракал." ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "ИМЯ, чуть не забыла. Папа хотел с тобой поговорить — он в гостиной. Подойди к нему." ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Хорошо. Что-то случилось?" ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Нет-нет, не переживай. Ему есть, что тебе рассказать. Думаю, тебе понравится." ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Мам, а какие-нибудь дела на сегодня есть?" ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Да, кстати. Когда вечером с прогулки будешь возвращаться, купи продукты по списку. Список и деньги пришлю как в магазине будешь." ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Ладно. А сдачу себе можно оставить? Хочу на кое-что накопить." ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Можно — но сначала продукты из списка. Знаю тебя: потратишь на что-нибудь ненужное, и придётся либо ещё денег давать, либо купишь по списку, но не совсем то, что нужно." ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Ну мам, это было пару раз всего… ну, может, три…" ),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc = "Мама сложила руки на груди и тяжело вздохнула — но в уголках губ пряталась улыбка." ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Ладно, но давай договоримся: разделим траты на обязательные и необязательные." ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Обязательные траты — это то, что нужно купить в любом случае. Без этого не обойтись. В нашей ситуации это продукты из списка.\n" ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Необязательные траты — это всё остальное: мороженое, лимонад, новые наушники, жетоны в парке аттракционов. Это то, без чего можно прожить. Хотя иногда очень хочется." ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Получается, если я ничего лишнего не куплю, то эти деньги можно отложить?" ),
    DialogLine_location(number_location = 1,number_npc = 3, text_npc = "Именно. Накопления — это деньги, которые ты не тратишь сейчас, чтобы использовать позже. Например, на крупную покупку или на непредвиденные расходы. Ты ведь сам решал, на что-то серьёзное накопить, помнишь?" ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Помню. Хорошо, я понял." ),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc = "В словарь добавлены новые определения: «Обязательные расходы», «Необязательные расходы», «Накопления»." ),
    DialogLine_location(number_location = 1,number_npc = 0, text_npc = "Словарь находится во вкладке «Термины». Каждый день в локациях и диалогах прячутся новые термины — найди их все! Прогресс изучения отображается во вкладке «Термины»." ),
)
val dialogLines_mother_evening = listOf(
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Мам, я купил продукты, как просила…" ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Хорошо, давай завтра с этим разберемся. Если все правильно купил, то дам в качестве награды 100 монет, за хорошую работу." ),
    DialogLine_location(number_location = 1,number_npc = 1, text_npc = "Ого, спасибо большое!" )
)
val dialogLines_Gera = listOf(
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "ИМЯ был в банке впервые без родителей — и это его будоражило. Внутри было тихо и строго: мягкий свет падал из потолочных ламп на удобные кресла, стойки с электронными номерами. Из скрытых динамиков тихо играл джаз. Пахло свежим кофе и новой бумагой — тем особенным запахом, который бывает только в учреждениях, где всё делают по правилам." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "У входа стоял терминал с экраном. ИМЯ замер перед ним, как перед новым уровнем в игре, провёл пальцем по иконкам: «Вклады», «Кредиты», «Переводы»… Потом увидел строчку «Получение карты» и нажал. Терминал прожужжал и выдал талончик с номером К14." ),
    DialogLine_location(number_location = 3,number_npc = 4, text_npc = "Ладно, народу немного. Все такие взрослые, серьёзные, с кучей документов, а у меня только паспорт в рюкзаке. Чувствую себя немного не в своей тарелке." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "На табло зажглось: К14. У ИМЯ ёкнуло в груди — как перед ответом у доски, только страшнее. Он подошёл к стойке. За ней сидел молодой сотрудник в строгой рубашке с закатанными рукавами. На столе рядом с монитором стоял кактус в маленьком горшке и кружка с надписью «#1 банк». На бейджике значилось: «Старший менеджер Орлов Герасим»." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Здравствуйте! Вы по какому вопросу?" ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Здравствуйте! На сайте вашего банка оставлял заявку на получение банковской карты. Пришло уведомление, что она готова." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Давайте сначала проверим вашу заявку. Мне понадобится ваш паспорт." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "ИМЯ достал паспорт из рюкзака и положил на стойку. Руки чуть подрагивали — впервые разбирался с чем-то серьёзным без родителей." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "Герасим раскрыл паспорт, посмотрел на страницу с фотографией — и вдруг поднял брови." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Подождите… ИМЯ? Вы же сын Михаила? Мы с ним вместе в институте учились! Он вам не рассказывал обо мне? Гера Орлов, помните, на дне рождения Миши был? " ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "ИМЯ прищурился — и правда, вспомнил." ),
    DialogLine_location(number_location = 3,number_npc = 4, text_npc = "Ничего себе! у папы вообще столько разных друзей — и на складе, и в банке… Никогда раньше не обращал на это внимания. Каждый раз новый человек — а оказывается, это все папины знакомые.." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Точно! Герасим… дядя Гера! А я вас не сразу узнал — вы тогда в шортах и футболке были, а тут в рубашке, при галстуке…" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Да, форма есть форма. Ну, рад тебя видеть! Так, давай посмотрим, что тут у тебя…" ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "Герасим сверил данные с экраном компьютера и с улыбкой кивнул." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Всё верно, карта готова. Ты, я вижу, волнуешься — впервые оформляешь собственную карту?" ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Да. Это моя первая личная банковская карта. Если я правильно понимаю, от детской она отличается тем, что у меня теперь собственный банковский счёт." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Именно! Ты взрослый парень, пора свои финансы в руках держать. Кстати, твой отец упоминал, что ты хочешь накопить на что-то серьёзное. Это правда?" ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Да. Я хочу накопить на крупную покупку. Только пока не знаю, как лучше это делать — откладывать понемногу или сразу большую сумму закинуть куда-нибудь." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Отличный настрой! Давай расскажу, какие есть варианты. А пока данные загружаются в системе — задавай вопросы, не стесняйся. Раз ты сын моего друга, для тебя никаких очередей." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Что такое банковский счёт?" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Банковский счёт — это твоя личная запись в системе банка, где хранятся твои деньги. У счёта есть уникальный номер из 20 цифр. Карта — это просто «ключ» к счёту. Если карту потеряешь — счёт останется, просто выпустят новую. Деньги никуда не денутся." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "То есть даже если я карту потеряю или сломаю, деньги на счёте останутся в безопасности?" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Именно. Карта — это просто пластик. Главное — что на счёте. А счёт привязан к тебе, а не к кусочку пластика." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Мне родители сказали оформить эту карту, чтобы научиться копить деньги. Как лучше это сделать? У меня есть конкретная цель, но не знаю, как правильнее откладывать." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Хороший вопрос! Для накоплений есть два основных способа: накопительный счёт и вклад." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Накопительный счёт — это счёт, на который можно класть и снимать деньги в любой момент. Никакого срока, никаких ограничений. Процент начисляется на остаток каждый день, но ставка может меняться — банк имеет право её пересматривать." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Вклад — это счёт, который открывается на конкретный срок: 3 месяца, 6 месяцев, год. Ставка фиксируется в момент открытия и не меняется до конца срока. Снимать деньги раньше срока можно, но обычно с потерей процентов." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Получается, разница только в том, хочу ли я снимать деньги в любой момент или готов подождать до конца срока?" ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "Герасим поднял палец — жест, которым обычно показывают: «в точку»." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "В этом вся суть. От вклада накопительный счёт отличается тем, что нет никакого срока. Положил сегодня, снял завтра, доложил через неделю — никаких ограничений. Процент начисляется на ту сумму, которая лежит в каждый конкретный день." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "У меня больше пока нет вопросов." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Карта готова! Теперь скачай наше приложение — без него не получится оценить все возможности карты" ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Уже установилось. Что делать дальше?" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Нужно зайти по номеру телефона — придёт код подтверждения." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "Герасим вдруг стал серьёзным." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "И запомни главное правило: если кто-нибудь — кто угодно — спросит у тебя код для входа в банк, ни за что его не сообщай. Это мошенники. Ни банк, ни я, ни твои родители никогда не попросим твой код." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Понял, запомню!" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "На главной странице видно всю краткую информацию: сколько денег на счёте, сколько накоплено кэшбэка и сколько на вкладе или накопительном счёте." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Во вкладке «Аналитика» видно, на что были потрачены деньги — по категориям: «Продукты», «Одежда и обувь», «Развлечения». Расходы показаны по дням и неделям, начиная с первого дня покупки." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Кэшбэк — это когда банк возвращает небольшой процент от суммы покупки обратно на счёт покупателя. В нашем банке кэшбэк действует на следующие категории трат: «Продукты», «Одежда и обувь», «Развлечения»." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Класс! То есть я покупаю продукты — и мне часть денег возвращается?" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Именно. Небольшой процент, но за месяц набегает прилично. Если пользоваться с умом — копить быстрее." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Во вкладке «Сбережения» можно отследить какую сумму уже получилось накопить." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Спасибо, теперь мне всё понятно! До свидания, дядя Гера!" ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Подожди, не убегай! Хочу вот что сказать: если у тебя появятся какие-нибудь вопросы про деньги, накопления, расходы — что угодно экономическое — приходи в банк и спрашивай. Мне не сложно, а тебе полезно. Ты же знаешь, где меня найти." ),
    DialogLine_location(number_location = 3, number_npc = 0, text_npc = "Герасим подмигнул и протянул ИМЯ визитку." ),
    DialogLine_location(number_location = 3,number_npc = 6, text_npc = "Здесь мой рабочий телефон и почта. Звони или пиши в любое время — или просто заходи. Двери всегда открыты." ),
    DialogLine_location(number_location = 3,number_npc = 1, text_npc = "Спасибо большое! Обязательно зайду, если что-то будет непонятно." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "ИМЯ убрал визитку в карман рюкзака, помахал Герасиму на прощание и вышел из банка на залитую солнцем улицу." ),
    DialogLine_location(number_location = 3,number_npc = 4, text_npc = "Что-то я устал, прежде чем пойти в магазин, нужно встретиться с Кешей и отдохнуть." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = "У ИМЯ есть шкала настроение. Ее хватает для посещения двух локаций в день." ),
    DialogLine_location(number_location = 3,number_npc = 0, text_npc = " Чтобы пополнить шкалу настроения и продолжить исследовать карту, нужно развлечься – это можно сделать встретившись с Кешей в парке." ),
)

val dialogLines_Kesha = listOf(
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Кеша! Привет! Прости, что так долго — я вообще не специально." ),
    DialogLine_location(number_location = 4,number_npc = 5, text_npc = "Да ладно, я привык. Ты вечно опаздываешь. Что на этот раз?" ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Сразу несколько дел навалилось. Сначала в банк ходил — карту получал, очередь была приличная. Потом на склад съездил — к другу отца, Владимиру. Он предложил мне там подрабатывать: бумажки перекладывать, данные в таблицы вбивать. Не тяжело, но времени сожрал кучу." ),
    DialogLine_location(number_location = 4,number_npc = 4, text_npc = "Плюс ещё этот Герасим со своими счетами, вкладами и кэшбэком — голова до сих пор гудит" ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "В общем, кататься на великах уже сил нет. Может просто погуляем?" ),
    DialogLine_location(number_location = 4,number_npc = 5, text_npc = "Не страшно, в другой раз покатаемся. Я никуда не тороплюсь — лето же." ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Слушай, банк — это вообще оказалось интереснее, чем я думал. Я думал там одни графики и цифры сплошные в куче с непонятными терминами. А оказалось, что это все такую комбинацию интересную создает… " ),
    DialogLine_location(number_location = 4,number_npc = 0, text_npc = "Потом он еще долго рассказывал про друзей отца, про подробности будущей работы, да так долго это было, что Кеша уже начал скучать и поэтому перебил:" ),
    DialogLine_location(number_location = 4,number_npc = 5, text_npc = "Ну, тебе виднее. Мне как-то без разницы на всю эту финансовую грамотность. Пока мне хочется развлекаться, гулять, а не в формулах копаться. Это же скучнее физики." ),
    DialogLine_location(number_location = 4,number_npc = 4, text_npc = "Ну, каждому своё. Может, потом сам поймёт. А может, и нет — и это тоже нормально." ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Ну, ты же знаешь, я тоже раньше вообще ни в этом не разбирался. Просто понял, что если сейчас не начну вникать, то когда в институт поступлю в будущем и буду жить самостоятельно, вообще не смогу нормально бюджет вести." ),
    DialogLine_location(number_location = 4,number_npc = 5, text_npc = "Ладно, понял я тебя. Давай лучше прогуляемся и о чем-нибудь другом поговорим. От этой твоей экономики и взрослой жизни голова пухнет." ),
    DialogLine_location(number_location = 4,number_npc = 0, text_npc = "Они поднялись со скамейки и неспешно двинулись по дорожке вглубь парка. Болтали о всякой ерунде: о том, что на лето дали кучу книжек, что стадион закрыли на ремонт, но скоро должны открыть — что Кеша нашёл на маркетплейсе прикольные наушники за триста монет и теперь ждёт посылку два месяца…" ),
    DialogLine_location(number_location = 4,number_npc = 0, text_npc = "После этого ИМЯ резко вспомнил, что мама просила в магазин зайти." ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Кеш, мне бежать надо. Забыл, мама просила в магазин зайти, а время уже позднее, магазин скоро закроется." ),
    DialogLine_location(number_location = 4,number_npc = 5, text_npc = "Ого, действительно поздно! Хорошо погуляли, встретимся тогда завтра и покатаемся наконец?" ),
    DialogLine_location(number_location = 4,number_npc = 1, text_npc = "Да, давай завтра встретимся тогда!" ),
)
val dialogLines_Vova = listOf(
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "Владимир, здравствуйте! Я ИМЯ, спасибо что вы разрешили у вас поработать." ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Ну, привет, ИМЯ! Рад, что пришёл. Проходи, не стой в дверях. Значит, так. У нас тут сотрудник уволился — тот, который документы в базу данных заносил. Хороший был работник, но нашёл место ближе к дому. Так что займешься этим ты. Не переживай, это не тяжёлый труд — тут голова нужна, а не спина" ),
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "А что именно нужно делать? Я с базами данных не работал никогда…" ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Тут нет ничего сложного. Главное — внимательность и не торопиться никуда. " ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Смотри: у нас на складе товары разных категорий — посуда, одежда, канцелярия, всякое. В одной таблице — список категорий, в другой — ячейки на складе, куда эти товары нужно поместить. Твоя задача — правильно связать товар с ячейкой, чтобы потом погрузчик легко нашёл нужную коробку." ),
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "То есть я как будто сортирую товары по полкам?" ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Именно! Только полки не в шкафу, а на целом складе. " ),
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "А если я ошибусь и выберу не ту ячейку?" ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Ничего страшного не случится — просто потом товар будет сложно найти, а это потеря времени. Поэтому и говорю: не торопись. Лучше проверить два раза, чем переделывать. Ошибки — это нормально, особенно в начале. Главное — учиться на них." ),
    DialogLine_location(number_location = 5,number_npc = 4, text_npc = "Звучит не так страшно, как я себе представлял. Всё-таки это не коробки таскать, а за компьютером сидеть. Почти как в игре — только за это ещё и платят." ),
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "Понял. Выглядит несложно, должен справиться." ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Вот и отлично. За работу заплачу, как писал в сообщении – 800 монет, если все сделаешь без ошибок, иначе получишь только минимальную оплату - 300 монет. Твой папа сказал, что ты копишь на что-то серьёзное, так что тебе лишние деньги не помешают, верно?" ),
    DialogLine_location(number_location = 5,number_npc = 1, text_npc = "Да, точно. Спасибо, Владимир, за возможность!" ),
    DialogLine_location(number_location = 5,number_npc = 7, text_npc = "Не за что. Твой папа мне в своё время помог — теперь моя очередь его пацану подсобить. Ну что, готов начать?" ),
)

@Composable
fun DialogScene_prolog(
    lines: List<DialogLine_location>,
    onComplete: () -> Unit
) {
    val currentIndex = rememberSaveable(lines) { mutableIntStateOf(0) }
    val currentLine = lines.getOrNull(currentIndex.value)
    if (currentLine == null) {
        LaunchedEffect(lines) { onComplete() }
        return
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize().testTag("prolog-dialogue")
                .clickable {
                    if (currentIndex.value < lines.size - 1) {
                        currentIndex.value++
                    } else {
                        onComplete()
                    }
                }
        ) {
            // ============================================================
            // 1. ФОН (зависит от локации)
            // ============================================================
            val backgroundRes = when (currentLine.number_location) {
                1 -> R.drawable.kitchen      // Кухня
                2 -> R.drawable.lounge_room  // Гостиная
                3 -> R.drawable.bank         // Банк
                4 -> R.drawable.park         // Парк
                5 -> R.drawable.storage      // Склад
                6 -> R.drawable.shop         // Магазин
                else -> R.drawable.lounge_room
            }

            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = "Фон",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Затемнение
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )

            // ============================================================
            // 2. ПЕРСОНАЖ + ТЕКСТ (зависит от NPC)
            // ============================================================
            when (currentLine.number_npc) {
                // ============================================================
                // 1 — НАШ ПЕРСОНАЖ (портрет слева, текст справа)
                // ============================================================
                1 -> {
                    Image(
                        painter = painterResource(id = R.drawable.pers_main),
                        contentDescription = "ИМЯ",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 2 — ОТЕЦ (портрет справа, текст слева)
                // ============================================================
                2 -> {
                    Image(
                        painter = painterResource(id = R.drawable.papa),
                        contentDescription = "Отец",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 3 — МАТЬ (портрет справа, текст слева)
                // ============================================================
                3 -> {
                    Image(
                        painter = painterResource(id = R.drawable.mama),
                        contentDescription = "Мать",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 5 — КЕША (портрет справа, текст слева)
                // ============================================================
                5 -> {
                    Image(
                        painter = painterResource(id = R.drawable.kesha),
                        contentDescription = "Кеша",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 6 — ГЕРА (портрет справа, текст слева)
                // ============================================================
                6 -> {
                    Image(
                        painter = painterResource(id = R.drawable.gera),
                        contentDescription = "Гера",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 7 — ВОВА (портрет справа, текст слева)
                // ============================================================
                7 -> {
                    Image(
                        painter = painterResource(id = R.drawable.vova),
                        contentDescription = "Вова",
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }

                // ============================================================
                // 0 — ОПИСАНИЕ (по центру)
                // ============================================================
                0 -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = currentLine.text_npc,
                            color = Color.White,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(32.dp)
                                .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                    }
                }

                // ============================================================
                // 4 — МЫСЛИ ГГ (по центру, курсив)
                // ============================================================
                4 -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = currentLine.text_npc,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(32.dp)
                                .background(Color(0xAA000000), RoundedCornerShape(8.dp))
                                .padding(16.dp)
                        )
                    }
                }

                // ============================================================
                // По умолчанию
                // ============================================================
                else -> {
                    Text(
                        text = currentLine.text_npc,
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Подсказка внизу
            Text(
                text = "Нажми, чтобы продолжить",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            )
        }
    }
}
@Composable
fun prolog_gane(
    gameProgress: Game_progress,
    onPrologComplete: () -> Unit
) {
    // Номер текущей сцены пролога
    val currentScene = rememberSaveable { mutableIntStateOf(1) }

    when (currentScene.value) {
        // ============================================================
        // СЦЕНА 1 — Утро, кухня (диалог с матерью)
        // ============================================================
        1 -> {
            DialogScene_prolog(
                lines = dialogLines_mother_morning,
                onComplete = { currentScene.value = 2 }
            )
        }

        // ============================================================
        // СЦЕНА 2 — Утро, гостиная (диалог с отцом)
        // ============================================================
        2 -> {
            DialogScene_prolog(
                lines = dialogLines_father_morning,
                onComplete = { currentScene.value = 3 }
            )
        }

        // ============================================================
        // СЦЕНА 3 — Банк (диалог с Герой)
        // ============================================================
        3 -> {
            DialogScene_prolog(
                lines = dialogLines_Gera,
                onComplete = { currentScene.value = 4 }
            )
        }

        // ============================================================
        // СЦЕНА 4 — Парк (диалог с Кешей)
        // ============================================================
        4 -> {
            DialogScene_prolog(
                lines = dialogLines_Kesha,
                onComplete = { currentScene.value = 5 }
            )
        }

        // ============================================================
        // СЦЕНА 5 — Склад (диалог с Вовой)
        // ============================================================
        5 -> {
            DialogScene_prolog(
                lines = dialogLines_Vova,
                onComplete = { currentScene.value = 6 }
            )
        }

        // ============================================================
        // СЦЕНА 6 — Вечер, гостиная (диалог с отцом)
        // ============================================================
        6 -> {
            DialogScene_prolog(
                lines = dialogLines_father_evening,
                onComplete = { currentScene.value = 7 }
            )
        }

        // ============================================================
        // СЦЕНА 7 — Вечер, кухня (диалог с матерью)
        // ============================================================
        7 -> {
            DialogScene_prolog(
                lines = dialogLines_mother_evening,
                onComplete = {
                    // 👈 Пролог завершён
                    onPrologComplete()
                }
            )
        }
    }
}
