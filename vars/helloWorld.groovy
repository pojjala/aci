def call(Map config = [:]){
 sh "echo ${config.name} How are you, today is ${config.dayOfWeek}" 
}
