package com.example.spacex.ui.map;

import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {
    public String getPlanetInfo(String planetName) {
        switch (planetName) {
            case "Sun":
                return "Солнце\nЗвезда класса G2V\nДиаметр: 1,392,684 км";
            case "Mercury":
                return "Меркурий\nБлижайшая к Солнцу планета\nДиаметр: 4,880 км";
            case "Venus":
                return "Венера\nСамая горячая планета\nДиаметр: 12,104 км";
            case "Earth":
                return "Земля\nЕдинственная обитаемая планета\nДиаметр: 12,742 км";
            case "Mars":
                return "Марс\nКрасная планета\nДиаметр: 6,779 км";
            case "Jupiter":
                return "Юпитер\nКрупнейшая планета\nДиаметр: 139,822 км";
            case "Saturn":
                return "Сатурн\nПланета с кольцами\nДиаметр: 116,464 км";
            case "Uranus":
                return "Уран\nЛедяной гигант\nДиаметр: 50,724 км";
            case "Neptune":
                return "Нептун\nСамый ветреный\nДиаметр: 49,244 км";
            default:
                return planetName;
        }
    }
}