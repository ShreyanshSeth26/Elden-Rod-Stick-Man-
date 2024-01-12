# **ELDEN ROD**
>The game is a copy of **Stick Hero** and a homage to 2022 GOTY **Elden Ring**.

## Authors 
> **Suryansh Varshney** - suryanshvarshney28@gmail.com


## Important Key Terms❗
* **Tarnished** (equivalent to Stick Hero)
* **Rune** (equivalent to cherries)
* **Rod** (equivalent to stick)

## How To Run The Game From CMD 👨🏻‍💻
Just running ```mvn javafx:run``` should do the trick, it should do all the compiling and running of the program.

## Game Mechanics 🎮
>There are two main game mechanics:
> 1. Click and hold the ```left mouse button``` to elongate the rod and release the same to stop elongation and drop
the rod.
> 2. While tarnished is moving, you can turn tarnished upside down by holding any ```keyboard key```, releasing which will
tarnished to get right side up. (don't rotate tarnished on a pillar, or he will die by hitting his head on the pillar 😢)
> 3. Grabbing `runes` increasing both the score and runes counter, and you need 250 runes to revive.  
> 
> Traverse 8 pillars to reach Boss arena!!!!!
> 1. `Right click` to defend
> 2. `Left click` to attack

## Testing 🧪
For testing, just run ```mvn verify``` or ```mvn test``` and testing will start.

## Design Patterns Used 🫧
We have used 2 design patterns in this project:
### 1. Singleton 1️⃣
    We have used this design pattern for Tarnished Class.
    We needed only one instance of this class because only one player
    would be playing this game at a time
### 2. Composite 🏘️
    The pillar class consists of a list of its own object which helps
    in some methods We have implemented.

## Multithreading 🧵
Threading is implemented many times in this project.
1. The constant rod elongation method until mouse key is released is done in a separate thread because the constant looping block
 in the method was crashing the application if called in the main thread.
2. The movement of tarnished is also done in a separate thread so that it can be parallelised with methods of rune collection.
3. As expected, method of rune collection is also in a separate thread in order to avoid crashing the appication because of the constant
looping block.
4. AI for the boss is multithreaded.
 
And many more.......

## Serialization 📩
Class Tarnished is actually serialized automatically when game is exited or Tarnished gets out and is deserialised when we got to menu-page.
Moreover, you can save the progress at any time in the game.

## [GitHub Repository Link 🔗](https://github.com/weebsuryansh/Elden-Rod)
