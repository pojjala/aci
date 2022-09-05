def call(Map config = [:]){
 sh "echo ${config.name} How are you, today is ${config.dayOfWeek}" 
}

def sayGoodMorning(){
 sh "echo  Calling from sayGoodMorning method"
}

def sayGoodEvening(){
 sh "echo  Calling from sayGoodEvening method"
}

def sayGoodNight(){
 sh "echo  Calling from sayGoodNight method"
}
