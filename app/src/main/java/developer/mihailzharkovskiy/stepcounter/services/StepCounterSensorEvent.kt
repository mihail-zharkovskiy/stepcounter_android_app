package developer.mihailzharkovskiy.stepcounter.services

sealed class StepCounterSensorEvent {
    object YesStepSensor : StepCounterSensorEvent()
    object NoSensorEvent : StepCounterSensorEvent()
}