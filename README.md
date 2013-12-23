Readme will be used to communicate and track ideas, bugs, etc

Sector25 (working name, hopefully someone has something better)

======== Game basics and ideas ~~~~~~~~~~
- Play starts a new game.  Games will mimick FTL in moving between planets and building up a character to play
	towards the final boss.  The character will have to go through 4-5 planet systems with a boss at the end of each.
	Health will not reset at all during play, but may be purchased or picked up.  The idea being that you don't expect
	a player to win the game very easily.  They will die many times and have to start over.
- Item system and upgrades have not been specced or thought out yet.  There likely will be money collected or points
	awarded which can be spent.  A system could be made without shops, but I'm not sure how to make it work well (like
	end of level random chance of getting items).  Upgrades would include move speed, health (and maybe shields),
	weapons and maybe other stuff (like pets/helpers that shoot?).  See curency section below.
- Enemies have also not been planned out.  Basically need to create a ton of them with different flight paths and
	different projectiles/attacks.  Mix them together per level or per system and get harder as you go.
	Flight paths can be kept simple for now.  I think it will be sufficient to have three types.  Head straight for
	the character (enemies without projectiles or just very aggressive), head generally towards the character but
	without intent on going straight at him (average projectile shooting enemy), and a more laissez-faire type which
	roam around and shoots when you come close (if you have enough of these evenly scattered around the map there will
	always be enemies in front of you).
- There is a switch for turning on individual health bars above enemies.  The default should be having them off, but
	we might allow it to be enabled if it doesn't affect the difficulty of the game.
- Arcade games also will be built similar to pew pew (awesome free android game).  Play for points or time survied
	type of stuff with bronze/silver/gold marks to beat.  Something easy to play for a few minutes without starting
	the real game.  Likely tie it to achievements as well.
- Scores screen local for now, divided between easy and normal modes (have to beat easy to unlock normal).
- Gameplay styles.  Each color of planet will represent a different kind of gameplay.  Easy ideas so far are Kills, 
	Time, and distance.  Distance should eventually spawn enemies based on distance rather than time, so if you go too
	fast you get swamped with enemies.  Going slow will be safe be not rewarding in points or score.
	Complex ideas will make the game a lot more interesting but represent new challenges with
	the implementation.  The next one to add could be defense of structures or conversely destroying structures.
	Another idea could be something like to recieve an item and come back.  Or go through some kind of maze.
	The last two would be extremely difficult to do without having static maps.  So we would either have to spend
	time making a complex dynamic map creation system or have enough static maps to cycle through.  Boss battles
	will also be somewhat unique.  Other gameplay ideas	are welcome.
	The idea of the level section being that a player can select a path to play the gametypes they like, try and hit
	the ones which they can get the most money, or avoid ones where they will be hurt too much.
- Currency will be tricky.  If it's from killing, then gameplay with killing will be more rewarding and gameplay
	without a time limit or kill limit will be broken.  An alternative is to have points be a type of currency.
	You could get points for finishing a level faster, which gives an insentive to do good rather than sit around
	killing.  A comprimise could be changing this based on gameplay type.  Kill levels could have money drops while
	distance levels give a money reward for fast play.  The idea of the game will be broken if any gameplay type
	allows the player to get an unfair amount of money.
- Objects will need to be coded to sit there and take up space.  Obvious one is just throwing some ateroids around.
	It comes with possibly coding ostacle avoidance for enemies.  Enemies should be able to fly through each other,
	and it's very possible that the we can just allow them to fly through anything.
- Graphics have been kept simple, everything so far being made in photoshop.  We don't want to steal anything.  For
	now I just want to implement enemies either with shitty photoshopping or with circles to replace later.  Even if
	designing shitty photoshops, keeping them circular will be easiest for dealing with hitboxes.  If anyone knows
	digital artist friends, see if they are interested.
- Story and cut scenes are possible?  Not my forte.
- Some kind of first time tutorial.
- Need to implement storage for keeping track of scores as well as continuing games.  Started with DataStore.
  Need to make sure this is secure as well if we plan on having non-local high scores.
- Allowing game to play correctly on many different screen sizes.  This isn't easy and I'm not sure what the
  best solution is to it.  Right now we'll just resize everything based on either width or height.  Hitboxes
  will need to resize with the images, right now they don't (and enemies aren't resized either yet).  Luckily
  we should have plenty of devices to test this on and can use emulators to look at the extremes eventually.
  Not all of our images even resize right now (projectiles and stuff), should double check at some point.
- Resource management.  Hopefully we can avoid this.  If the number of graphics stay small enough it can all be in
	memory.  Might just have to restrict devices to 4.0+ to assure they have sufficient memory space and processing
	power.  Otherwise we could at least implement loading enemy bitmaps depending on if they are needed for that
	level.  Backgrounds are particularly taxing, which is why the stars are drawn programatically rather than having
	another couple giant bitmaps.


	Any other questions on the basic ideas, let's plan it out here.	

	
Future versions:
- Plan to have the game extended to ouya/android consoles with multiple players on one screen (not split screen).
	Joysticks should be easy to replace virtual with physical.  All buttons have been implemented with ints, easy
	to swap out touch for scrolling through selection ints.