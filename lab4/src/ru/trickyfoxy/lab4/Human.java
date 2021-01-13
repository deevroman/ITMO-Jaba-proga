package ru.trickyfoxy.lab4;

abstract public class Human implements Named {
    private final String name;
    private final Gender gender;

    public String getName() {
        return name;
    }


    public Human(String name, Gender gender) {
        this.name = name;
        this.gender = gender;
    }

    public void scream(String text) {
        class SelectorCorrectWord {
            class GenderEnds {
                public String getEnd(Gender gender) {
                    if (gender == Gender.FEMALE) return "а";
                    else return "";
                }
            }

            public String getScreamVerb() {
                return "крикнул" + new GenderEnds().getEnd(gender);
            }
        }
        SelectorCorrectWord selector = new SelectorCorrectWord();
        System.out.println(this.name + " " + selector.getScreamVerb() + ": \"" + text + "\"");
    }
}
