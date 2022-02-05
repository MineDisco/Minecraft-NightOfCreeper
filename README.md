# Minecraft-NightOfCreeper-Bukkit
![](https://img.shields.io/badge/Minecraft-1.18.1-blue.svg?style=flat-square)

Can you beat Minecraft full of Creepers? 

## Features 
* Always night
* Creepers, creepers, a lot of creepers.. (Also Ghasts)

![Creeper1](/creeper1.png)
![Creeper2](/creeper2.png)

## Settings
### server.properties
* difficulty=normal

### spigot.yml
```yml
world-settings:
  default:
        mob-spawn-range: 16
        entity-tracking-range:
            monsters: 110
            misc: 50
        entity-activation-range:
            monsters: 102
            wake-up-inactive:
                monsters-max-per-tick: 32
                monsters-every: 200
```

### paper.yml
```yml
world-settings:
  default:
    entities-target-with-follow-range: true
```











