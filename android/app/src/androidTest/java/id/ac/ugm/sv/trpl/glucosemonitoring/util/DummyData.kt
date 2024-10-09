package id.ac.ugm.sv.trpl.glucosemonitoring.util

import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.EventType
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.enums.GlucoseCategory
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Event
import id.ac.ugm.sv.trpl.glucosemonitoring.domain.model.Glucose

object DummyData {
    
    const val ALL_GLUCOSES_SIZE = 14
    const val ALL_GLUCOSES_LAST_VALUE = 50
    const val ALL_GLUCOSES_AVERAGE = 131
    const val ALL_GLUCOSES_MAX = 260
    const val ALL_GLUCOSES_MIN = 50
    const val ALL_GLUCOSES_TIR = 43
    const val ALL_GLUCOSES_TAR = 29
    const val ALL_GLUCOSES_TBR = 29
    val ALL_GLUCOSES_LAST_CATEGORY = GlucoseCategory.HYPOGLYCEMIA_LEVEL_2
    const val ALL_GLUCOSES_LAST_TIME = "12:00"
    
    const val DUMMY_DATE_1_GLUCOSES_SIZE = 7
    const val DUMMY_DATE_1_GLUCOSES_LAST_VALUE = 260
    const val DUMMY_DATE_1_GLUCOSES_AVERAGE = 131
    const val DUMMY_DATE_1_GLUCOSES_MAX = 260
    const val DUMMY_DATE_1_GLUCOSES_MIN = 50
    const val DUMMY_DATE_1_GLUCOSES_TIR = 43
    const val DUMMY_DATE_1_GLUCOSES_TAR = 29
    const val DUMMY_DATE_1_GLUCOSES_TBR = 29
    
    const val DUMMY_DATE_2_GLUCOSES_SIZE = 7
    const val DUMMY_DATE_2_GLUCOSES_LAST_VALUE = 50
    const val DUMMY_DATE_2_GLUCOSES_AVERAGE = 131
    const val DUMMY_DATE_2_GLUCOSES_MAX = 260
    const val DUMMY_DATE_2_GLUCOSES_MIN = 50
    const val DUMMY_DATE_2_GLUCOSES_TIR = 43
    const val DUMMY_DATE_2_GLUCOSES_TAR = 29
    const val DUMMY_DATE_2_GLUCOSES_TBR = 29
    
    val glucose1 = Glucose(
        id = 1,
        level = 50f,
        date = Constants.DUMMY_DATE_1,
        time = "06:00",
    )
    
    val glucose2 = Glucose(
        id = 2,
        level = 60f,
        date = Constants.DUMMY_DATE_1,
        time = "07:00",
    )
    
    val glucose3 = Glucose(
        id = 3,
        level = 70f,
        date = Constants.DUMMY_DATE_1,
        time = "08:00",
    )
    
    val glucose4 = Glucose(
        id = 4,
        level = 100f,
        date = Constants.DUMMY_DATE_1,
        time = "09:00",
    )
    
    val glucose5 = Glucose(
        id = 5,
        level = 175f,
        date = Constants.DUMMY_DATE_1,
        time = "10:00",
    )
    
    val glucose6 = Glucose(
        id = 6,
        level = 200f,
        date = Constants.DUMMY_DATE_1,
        time = "11:00",
    )
    
    val glucose7 = Glucose(
        id = 7,
        level = 260f,
        date = Constants.DUMMY_DATE_1,
        time = "12:00",
    )
    
    val glucose8 = Glucose(
        id = 8,
        level = 260f,
        date = Constants.DUMMY_DATE_2,
        time = "06:00",
    )
    
    val glucose9 = Glucose(
        id = 9,
        level = 200f,
        date = Constants.DUMMY_DATE_2,
        time = "07:00",
    )
    
    val glucose10 = Glucose(
        id = 10,
        level = 175f,
        date = Constants.DUMMY_DATE_2,
        time = "08:00",
    )
    
    val glucose11 = Glucose(
        id = 11,
        level = 100f,
        date = Constants.DUMMY_DATE_2,
        time = "09:00",
    )
    
    val glucose12 = Glucose(
        id = 12,
        level = 70f,
        date = Constants.DUMMY_DATE_2,
        time = "10:00",
    )
    
    val glucose13 = Glucose(
        id = 13,
        level = 60f,
        date = Constants.DUMMY_DATE_2,
        time = "11:00",
    )
    
    val glucose14 = Glucose(
        id = 14,
        level = 50f,
        date = Constants.DUMMY_DATE_2,
        time = "12:00",
    )
    
    val allGlucoses = listOf(
        glucose1,
        glucose2,
        glucose3,
        glucose4,
        glucose5,
        glucose6,
        glucose7,
        glucose8,
        glucose9,
        glucose10,
        glucose11,
        glucose12,
        glucose13,
        glucose14,
    )
    
    val dummyDate1Glucoses = listOf(
        glucose1,
        glucose2,
        glucose3,
        glucose4,
        glucose5,
        glucose6,
        glucose7,
    )
    
    val dummyDate2Glucoses = listOf(
        glucose8,
        glucose9,
        glucose10,
        glucose11,
        glucose12,
        glucose13,
        glucose14,
    )
    
    private const val DUMMY_DESC = "Lorem ipsum"
    
    const val ALL_EVENTS_SIZE = 6
    const val DUMMY_DATE_1_EVENTS_SIZE = 3
    const val DUMMY_DATE_2_EVENTS_SIZE = 3
    
    const val ALL_EVENTS_LAST_MEAL = 4
    const val ALL_EVENTS_LAST_EXERCISE = 5
    const val ALL_EVENTS_LAST_MEDICATION = 6
    
    val EVENT_1_RELATED_GLUCOSE_DATA = listOf(1, 2, 3, 4)
    val EVENT_2_RELATED_GLUCOSE_DATA = listOf(2, 3, 4, 5, 6, 7, 8, 9, 10)
    val EVENT_3_RELATED_GLUCOSE_DATA = listOf(5, 6, 7, 8, 9, 10, 11, 12, 13)
    val EVENT_4_RELATED_GLUCOSE_DATA = listOf(8, 9, 10, 11)
    val EVENT_5_RELATED_GLUCOSE_DATA = listOf(9, 10, 11, 12, 13, 14)
    val EVENT_6_RELATED_GLUCOSE_DATA = listOf(12, 13, 14)
    
    const val EVENT_1_INITIAL_GLUCOSE = 60
    const val EVENT_2_INITIAL_GLUCOSE = 70
    const val EVENT_3_INITIAL_GLUCOSE = 200
    const val EVENT_4_INITIAL_GLUCOSE = 200
    const val EVENT_5_INITIAL_GLUCOSE = 175
    const val EVENT_6_INITIAL_GLUCOSE = 60
    
    const val EVENT_1_FINAL_GLUCOSE = 100
    val EVENT_2_FINAL_GLUCOSE: Int? = null
    val EVENT_3_FINAL_GLUCOSE: Int? = null
    const val EVENT_4_FINAL_GLUCOSE = 100
    val EVENT_5_FINAL_GLUCOSE: Int? = null
    val EVENT_6_FINAL_GLUCOSE: Int? = null
    
    const val EVENT_1_GLUCOSE_CHANGES = 40
    val EVENT_2_GLUCOSE_CHANGES: Int? = null
    val EVENT_3_GLUCOSE_CHANGES: Int? = null
    const val EVENT_4_GLUCOSE_CHANGES = -100
    val EVENT_5_GLUCOSE_CHANGES: Int? = null
    val EVENT_6_GLUCOSE_CHANGES: Int? = null
    
    val EVENT_1_RELATED_EVENT_DATA = listOf(2)
    val EVENT_2_RELATED_EVENT_DATA = listOf(1, 3, 4, 5)
    val EVENT_3_RELATED_EVENT_DATA = listOf(2, 4, 5, 6)
    val EVENT_4_RELATED_EVENT_DATA = listOf(2, 3, 5)
    val EVENT_5_RELATED_EVENT_DATA = listOf(2, 3, 4, 6)
    val EVENT_6_RELATED_EVENT_DATA = listOf(3, 5)
    
    val event1 = Event(
        id = 1,
        type = EventType.MEAL,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_1,
        time = "07:00",
    )
    
    val event2 = Event(
        id = 2,
        type = EventType.EXERCISE,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_1,
        time = "08:00",
    )
    
    val event3 = Event(
        id = 3,
        type = EventType.MEDICATION,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_1,
        time = "11:00",
    )
    
    val event4 = Event(
        id = 4,
        type = EventType.MEAL,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_2,
        time = "07:00",
    )
    
    val event5 = Event(
        id = 5,
        type = EventType.EXERCISE,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_2,
        time = "08:00",
    )
    
    val event6 = Event(
        id = 6,
        type = EventType.MEDICATION,
        desc = DUMMY_DESC,
        date = Constants.DUMMY_DATE_2,
        time = "11:00",
    )
    
    val allEvents = listOf(
        event1,
        event2,
        event3,
        event4,
        event5,
        event6,
    )
    
    val dummyDate1Events = listOf(
        event1,
        event2,
        event3,
    )
    
    val dummyDate2Events = listOf(
        event4,
        event5,
        event6,
    )
    
    object HealthNumbers {
        const val WEIGHT = 85
        const val HEIGHT = 175
        const val BMI = 27.8
        const val SYSTOLIC = 120
        const val DIASTOLIC = 70
    }
    
}