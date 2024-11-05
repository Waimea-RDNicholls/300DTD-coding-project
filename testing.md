# Test Plan and Evidence / Results of Testing

## Game Description

The project involves the programming of a singleplayer game called MS Paint Escape.
The objective is to find the right items to finally access the exit. Players play by using
the directional movement buttons to change location, whereupon they may find items or other clues. They can press a
button to use an item, if the location allows it. Upon reaching the elevator and powering it, the player wins.


### Game Features and Rules

The game has the following features and/or rules:

- Every location is generated, and connected to each other
- Items are distributed to each location
- The player may move North, South, East or West, if:
    - The location they want to travel to is not null
    - The location allows passage/is unlocked
    - They were at that location previously
- Players can find items at locations, which they collect automatically
- Players may use an item, if the location is unlocked by that item
- Players can see which items they have by looking at their inventory
- Players win upon reaching the final location. They can then replay, if they so choose.

---

## Test Plan

The following game features / functionality and player actions will need to be tested:

- Map Creation: All locations should be connected in a set way, with the right item, and be unlocked by the right item.
- Movement: The player should only be allowed to input valid moves. Any invalid moves should be rejected.
- Items: The player should automatically gather items if the location has any. They should be able to see what items they
have by looking at their inventory. Their inventory should be capable of displaying all items at the same time.
- Victory: The player should be able to win. The victory button should lock movement. The play again button should restart the game.

The following tests will be run against the completed game. The tests should result in the expected outcomes shown.


### Map Generation

The map should generate the same way every time, with the same items in the same spots

#### Test Data / Actions to Use

Start the game several times

#### Expected Outcome

The map should be the same every time. The item locations should be the same. The locked locations should be unlocked
by the same items every time. They should have the same display image, every time.


### Movement

The player should only be allowed to make valid movements. Invalid movements should not work.

#### Test Data / Actions to Use

Valid moves:
Try to move to a valid location e.g an unlocked space to the north, south, east and west.

Edge-case valid moves:
Try to move to and from an edge-case location, e.g the first location in the list (home), or the last location in the list (crater)

Invalid moves:
Try to move to a 'null' location.
Try to move past a locked location.
Try to move when you've won.


#### Expected Outcome

Valid moves should move the player to the chosen location.

Invalid moves should not allow input in the first place, and therefore not move the player.


### Picking up items

The player should automatically pick up items in locations.


#### Test Data / Actions to Use

Move to a location that has an item.
Move to a location that hasn't got an item.

#### Expected Outcome

The player should be alerted that they've picked up an item, if in the right location. This item should be added
to their inventory.


### Using items

The player should be able to use items to unlock locations.

#### Test Data / Actions to Use

Move to a locked location.
Valid input: Try to use the button with the right item.
Invalid input: Try to use the button without the right item.

Try to unlock an already unlocked location.

#### Expected Outcome

Unlocked locations do not show a 'use item' button. Locked locations do.
When the right item is used, the location will unlock. Otherwise, you cannot use the unlock button.


### Inventory Display

The player should be able to see all the items they've got in their inventory.

## Test Data / Actions to Use

Valid input: Acquire any number of items
Edge-case: Acquire the maxiumum number of items. Have no items.

#### Expected Outcome

The inventory should display 'nothing' if the player has no items. Otherwise, it should display every item
the player has, in a list.


### Victory

The player should be able to win, when they reach the final location. They should then be able to replay.

## Test Data / Actions to Use

Go to the 'win' location.
Press the 'replay' button.
Play again, and win again.

#### Expected Outcome

The player should see a notification, stating 'they win.'
They should be prompted to play again, and be able to then restart the game.
When replaying, the map should remain the same, and they should be able to win again.


---


## Evidence / Results of Testing

### Map Generation

ACTUAL RESULTS OF TESTING SHOWN HERE

![](src/images/placeholder.jpg)

NOTES REGARDING THE RESULTS HERE


### Movement

ACTUAL RESULTS OF TESTING SHOWN HERE

![](src/images/placeholder.jpg)

NOTES REGARDING THE RESULTS HERE


### Inventory

ACTUAL RESULTS OF TESTING SHOWN HERE

![](src/images/placeholder.jpg)

NOTES REGARDING THE RESULTS HERE


### Winning

ACTUAL RESULTS OF TESTING SHOWN HERE

![](src/images/placeholder.jpg)

NOTES REGARDING THE RESULTS HERE

