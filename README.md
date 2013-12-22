Readme will be used to communicate and track ideas, bugs, etc

sector25 (working name, hopefully someone has something better)
======== Game basics and ideas ~~~~~~~~~~
- Play starts a new game.  Games will mimick FTL in moving between planets and building up a character to play
	towards the final boss.  The character will have to go through 4-5 planet systems with a boss at the end of each.
	Health will not reset at all during play, but may be purchased or picked up.  The idea being that you don't expect
	a player to win the game very easily.  They will die many times and have to start over.
- Item system and upgrades have not been specced or thought out yet.  There likely will be money collected or points
	awarded which can be spent.  A system could be made without shops, but I'm not sure how to make it work well (like
	end of level random chance of getting items).  Upgrades would include move speed, health (and maybe shields),
	weapons and maybe other stuff (like pets/helpers that shoot?).
- Enemies have also not been planned out.  Basically need to create a ton of them with different flight paths and
	different projectiles/attacks.  Mix them together per level or per system and get harder as you go.
	Flight paths can be kept simple for now.  I think it will be sufficient to have three types.  Head straight for
	the character (enemies without projectiles or just very aggressive), head generally towards the character but
	without intent on going straight at him (average projectile shooting enemy), and a more laissez-faire type which
	roam around and shoots when you come close (if you have enough of these evenly scattered around the map there will
	always be enemies in front of you).
- Arcade games also will be built similar to pew pew (awesome free android game).  Play for points or time survied
	type of stuff with bronze/silver/gold marks to beat.
- Scores screen local for now, divided between easy and normal modes (have to beat easy to unlock normal).
- Each color of planet will represent a different kind of gameplay.  Easy ideas so far are Kills, Time, 
	and distance.  Complex ideas will make the game a lot more interesting but represent new challenges with
	the implementation.  The next one to add could be defense of structures or conversely destroying structures.
	Another idea could be something like to recieve an item and come back.  Or go through some kind of maze.
	The last two would be extremely difficult to do without having static maps.  So we would either have to spend
	time making a complex dynamic map creation system or have enough static maps to cycle through.  Boss battles
	will also be somewhat unique.  Other gameplay ideas	are welcome.
	The idea of the level section being that a player can select a path to play the gametypes they like, try and hit
	the ones which they can get the most money, or avoid ones where they will be hurt too much.
- Objects will need to be coded to sit there and take up space.  Obvious one is just throwing some ateroids around.
	It comes with possibly coding ostacle avoidance for enemies.  Enemies should be able to fly through each other,
	and it's very possible that the we can just allow them to fly through anything.

- Graphics have been kept simple, everything so far being made in photoshop.  We don't want to steal anything.  For
	now I just want to implement enemies either with shitty photoshopping or with circles to replace later.  Even if
	designing shitty photoshops, keeping them circular will be easiest for dealing with hitboxes.  If anyone knows
	digital artist friends, see if they are interested.

	Any other questions on the basic ideas, let's plan it out here.	

	
Future versions:
- Plan to have the game extended to ouya/android consoles with multiple players on one screen (not split screen).