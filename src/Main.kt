/**
 * ------------------------------------------------------------------------
 * Microsoft Paint Escape
 * Level 3 programming project
 *
 * by Riley ########
 *
 *
 *
 *
 * ------------------------------------------------------------------------
 */


import com.formdev.flatlaf.FlatDarkLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*


//=============================================================================================

// Setup 'location' class for use as map
class Location(val name: String, val description: String) {
    // Setup variables for items at location and locations to the N S E W
    var item: Item? = null
    var north: Location? = null
    var south: Location? = null
    var east: Location? = null
    var west: Location? = null

    // Lock variable setup
    var canGoPast: Boolean = true
    var unlockedBy: Item? = null
    // Image variable setup
    var lockedImageLocated: String? = null
    var unlockedImageLocated: String? = null

    // Functions to add connections to other locations
    fun addLocationNorth(location: Location) {
        if (north == null) {
            north = location
            location.addLocationSouth(this)
        }
    }
    fun addLocationSouth(location: Location) {
        if (south == null) {
            south = location
            location.addLocationNorth(this)
        }
    }
    fun addLocationEast(location: Location) {
        if (east == null) {
            east = location
            location.addLocationWest(this)
        }
    }
    fun addLocationWest(location: Location) {
        if (west == null) {
            west = location
            location.addLocationEast(this)
        }
    }

    // Function to lock location + add key
    fun unlockedBy(item: Item) {
        if (this.canGoPast) {
            this.canGoPast = false
            this.unlockedBy = item
        }
    }

    // Functions to add image to location
    fun addLockedImage(imageName: String) {
        if (this.lockedImageLocated == null) {
            this.lockedImageLocated = "src/images/$imageName.jpg"
        }
    }
    fun addUnlockedImage(imageName: String) {
        if (this.unlockedImageLocated == null) {
            this.unlockedImageLocated = "src/images/$imageName.jpg"
        }
    }

    // Debug function to show details about location
    fun info() {
        println("Location name: $name.")
        // Display what this location is connected to
        if (north != null) {
            println("To the north is ${north!!.name}")
        } else { println("I have no location north.") }
        if (south != null) {
            println("To the south is ${south!!.name}")
        } else { println("I have no location south.") }
        if (east != null) {
            println("To the east is ${east!!.name}")
        } else { println("I have no location east.") }
        if (west != null) {
            println("To the west is ${west!!.name}")
        } else { println("I have no location west.") }

        // Display items found at location
        if (item != null) {
            println("I have ${item!!.name}")
        } else { println("I have no item.") }
        // Display what unlocks this location
        if (!canGoPast) {
            println("You cannot go past me. I'm unlocked by ${unlockedBy!!.name}")
        } else {println("You can go past me.")}
        println("---------------------------------------")
    }

}
class Item(val name: String) {
    // Where item is found
    var located: Location? = null

    // Function to add item to a location
    fun addItemTo(location: Location) {
        if (location != null) {
            location.item = this
            located = location
        }
    }


}


/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI : JFrame(), ActionListener {
    // Create list of map locations
    val locations = mutableListOf<Location>()

    var currentLocation: Location
    var previousLocation: Location? = null

    // Set up player inventory
    val inventory = mutableListOf<Item>()

    // Setup some properties to hold the UI elements

    // Current Location Display
    private lateinit var currentLocationLabel: JLabel
    private lateinit var locationDescriptionLabel: JLabel
    private lateinit var locationImageLabel: JLabel

    // Movement Buttons
    private lateinit var goNorthButton: JButton
    private lateinit var goSouthButton: JButton
    private lateinit var goEastButton: JButton
    private lateinit var goWestButton: JButton

    // Inventory Display
    private lateinit var inventoryDisplayLabel: JLabel
    private lateinit var unlockLocationButton: JButton
    private var foundItemDialog = FoundItemDialog()

    // Win Dialog + Replay Button
    private var winDialog = WinDialog()
    private lateinit var replayButton: JButton



    /**
     * Create, build and run the UI
     */
    init {
        // Create locations, map, items
        setupGame()
        // Create GUI
        setupWindow()
        buildUI()

        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true

        currentLocation = locations.first()
        showLocation()
    }
    private fun setupGame() {
        // Create locations
        // 'Top half' locations
        val manor = Location("Manor", "An imposing building, large as can be, stands in front of you." +
                " A metal fence encircles the manor, guarding it's contents. The door itself seems to be barred with" +
                " solid steel. You don't think you can break through, unless you had some kind of drill.")
        val ladder = Location("Ladder", "A tall ladder. It goes up a fair distance, further than you can" +
                " see... hopefully you don't have to climb for too long.")
        val ladderBase = Location("Ladder Base", "A sign reads, 'Path to the Manor.' You see a ladder next " +
                "to the sign, one that seems to stretch on further than you can believe. To go up, or not?")
        val ridges = Location("Ridges", "Narrow ridges jut out, just wide enough for you to navigate, above " +
                "the battlefield. It seems to lead further up, towards a platform.")
        val pipes = Location("Pipes", "Large pipes stand high above the ground. They seem stable enough to " +
                "walk across.")
        val aquarium = Location("Aquarium", "This place has so much water! No wonder there were pipes on the " +
                "way here. But this also looks surprisingly high-tech... there must be a control room directly nearby!")
        val controlRoom = Location("Control Room", "This place isn't high tech at all! There's just some " +
                "dinky batteries laying around. Like these would ever be useful.")
        val field = Location("Field", "What a nice field. Can see everything in the distance from here. " +
                "That tower looks important.")
        val tower = Location("Tower", "Hey look, the tower! This must be the way free! The door requires a " +
                "keycard to activate.")
        val elevator = Location("Elevator", "Now, to just go up this thing...why does it have no power? " +
                "Why does this thing accept AA batteries? This can't be up to health and safety standards...")
        val win = Location("Freedom!!!", "Hey, why hasn't the artstyle changed?!")
        val manorRoom = Location("Grand Manor Room", "This manor seems awfully small compared to the outside. " +
                "Why is there only one room? There's some fancy paintings at least. And a keycard? This might be useful.")




        // 'Bottom half' locations
        val battlefield = Location("Battlefield", "Oh, the humanity! We can't show this to anyone! We'll get " +
                "an age rating of 18+! Avert your eyes! At least there seems to be something useful amongst all this viscera...")
        val plane = Location("Plane Wreck", "Amongst the snowy land, a massive skeleton of a plane lies " +
                "in wait, almost as if it seeks to ensnare passersby. It's just a plane, though. Can't do much.")
        val home = Location("Home..?", "This is home. But why is everything in such low quality? What the " +
                "hell happened here? What's that tower, far to the north? Maybe I can find directions somewhere to the " +
                "south-east...")
        val emptyField = Location("Empty Field", "There is literally nothing here. What? Where did that " +
                "horrible low quality art go?")
        val sewer = Location("Sewer", "Why would you even want to go here? This place STINKS!")
        val apartments = Location("Apartments", "The people living here were seriously handy... they " +
                "even had a map, for some reason. Arrows seem to mark the map, with an image of a ruined plane " +
                "acting as a starting point. The arrows are as follows: ")
        val darkTunnel = Location("Dark Tunnel", "Where are the lights!?")
        val bunker = Location("Bunker", "What a nice place. Fancy there being a ladder here. This place " +
                "is stocked to the brim with all sorts of goods, as well. But upon closer inspection, all of these cans " +
                "of beans have gone off...")
        val crater = Location("Crater", "What a large crater. Wonder what caused this.")

        // Create items
        val batteries = Item("Batteries")
        val keyCard = Item("Key Card")
        val drill = Item("Drill")

        // Add locations to mutable list
        // Starting location
        locations.add(home)

        // Top half locations
        locations.add(manor)
        locations.add(ladder)
        locations.add(ladderBase)
        locations.add(ridges)
        locations.add(pipes)
        locations.add(aquarium)
        locations.add(controlRoom)
        locations.add(field)
        locations.add(tower)
        locations.add(elevator)
        locations.add(win)
        locations.add(manorRoom)

        // Bottom half locations
        locations.add(battlefield)
        locations.add(plane)
        locations.add(emptyField)
        locations.add(sewer)
        locations.add(apartments)
        locations.add(darkTunnel)
        locations.add(bunker)
        locations.add(crater)


        // Create map by connecting locations to each other
        // Top half locations
        ladder.addLocationNorth(manor)
        ladderBase.addLocationNorth(ladder)
        pipes.addLocationEast(ladderBase)
        aquarium.addLocationEast(pipes)
        controlRoom.addLocationNorth(aquarium)
        field.addLocationEast(aquarium)
        tower.addLocationEast(field)
        elevator.addLocationNorth(tower)
        ridges.addLocationNorth(ladderBase)
        win.addLocationNorth(elevator)
        manorRoom.addLocationSouth(manor)

        // Bottom half locations
        battlefield.addLocationNorth(ridges)
        plane.addLocationEast(battlefield)
        home.addLocationNorth(plane)
        sewer.addLocationEast(plane)
        emptyField.addLocationNorth(sewer)
        emptyField.addLocationEast(home)
        apartments.addLocationNorth(emptyField)
        crater.addLocationWest(battlefield)
        crater.addLocationEast(bunker)
        bunker.addLocationSouth(darkTunnel)
        darkTunnel.addLocationWest(home)



        // Lock locations, distribute items to locations
        batteries.addItemTo(controlRoom)
        elevator.unlockedBy(batteries)

        keyCard.addItemTo(manorRoom)
        tower.unlockedBy(keyCard)

        drill.addItemTo(bunker)
        manor.unlockedBy(drill)

        // Add display images to locations
        home.addUnlockedImage("home")
        manor.addUnlockedImage("manorUnlocked")
        manor.addLockedImage("manorLocked")
        manorRoom.addUnlockedImage("manorRoom")
        aquarium.addUnlockedImage("aquarium")
        plane.addUnlockedImage("plane")
        elevator.addLockedImage("elevatorLocked")
        elevator.addUnlockedImage("elevatorUnlocked")
        field.addUnlockedImage("field")
        pipes.addUnlockedImage("pipes")
        ladderBase.addUnlockedImage("ladderBase")
        ladder.addUnlockedImage("ladder")
        battlefield.addUnlockedImage("battlefield")
        sewer.addUnlockedImage("sewer")
        bunker.addUnlockedImage("bunker")
        darkTunnel.addUnlockedImage("darkTunnel")
        apartments.addUnlockedImage("apartments")
        crater.addUnlockedImage("crater")
        ridges.addUnlockedImage("ridges")
        tower.addLockedImage("towerLocked")
        tower.addUnlockedImage("towerUnlocked")
        win.addUnlockedImage("win")
        controlRoom.addUnlockedImage("controlRoom")








    }

    /**
     * Configure the main window
     */
    private fun setupWindow() {
        title = "MS Paint Escape"
        contentPane.preferredSize = Dimension(1000, 750)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI
     */
    private fun buildUI() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)


        //---LOCATION DISPLAY LABELS -----------------------------------------------------------------------------------
        currentLocationLabel = JLabel("CURRENT LOCATION", SwingConstants.LEFT)
        currentLocationLabel.bounds = Rectangle(265, 0, 735, 56)
        currentLocationLabel.font = baseFont
        add(currentLocationLabel)


        locationDescriptionLabel = JLabel("CURRENT LOCATION DESCRIPTION", SwingConstants.LEFT)
        locationDescriptionLabel.setVerticalAlignment(SwingConstants.TOP)
        locationDescriptionLabel.bounds = Rectangle(265, 56, 735, 194)
        locationDescriptionLabel.font = baseFont
        add(locationDescriptionLabel)

        var locationImage = ImageIcon("src/images/").image
        locationImage = locationImage.getScaledInstance(250,250, Image.SCALE_SMOOTH)
        locationImageLabel = JLabel()
        locationImageLabel.bounds = Rectangle(0, 0, 250, 250)
        locationImageLabel.icon = ImageIcon(locationImage)
        add(locationImageLabel)

        //---INVENTORY DISPLAY LABEL -----------------------------------------------------------------------------------
        inventoryDisplayLabel = JLabel("CURRENT INVENTORY")
        inventoryDisplayLabel.setVerticalAlignment(SwingConstants.TOP)
        inventoryDisplayLabel.setHorizontalAlignment(SwingConstants.LEFT)
        inventoryDisplayLabel.bounds = Rectangle(0, 273, 250, 477)
        inventoryDisplayLabel.font = baseFont
        add(inventoryDisplayLabel)




        //---NAVIGATION BUTTONS ----------------------------------------------------------------------------------------
        goNorthButton = JButton("CURRENT LOCATION NORTH")
        goNorthButton.bounds = Rectangle(535,297,183,115)
        goNorthButton.font = baseFont
        goNorthButton.addActionListener(this)
        add(goNorthButton)

        goSouthButton = JButton("CURRENT LOCATION SOUTH")
        goSouthButton.bounds = Rectangle(535,573,183,115)
        goSouthButton.font = baseFont
        goSouthButton.addActionListener(this)
        add(goSouthButton)

        goEastButton = JButton("CURRENT LOCATION EAST")
        goEastButton.bounds = Rectangle(718,435,183,115)
        goEastButton.font = baseFont
        goEastButton.addActionListener(this)
        add(goEastButton)

        goWestButton = JButton("CURRENT LOCATION WEST")
        goWestButton.bounds = Rectangle(352,435,183,115)
        goWestButton.font = baseFont
        goWestButton.addActionListener(this)
        add(goWestButton)

        unlockLocationButton = JButton("UNLOCK CURRENT LOCATION")
        unlockLocationButton.bounds = Rectangle(555, 445, 143, 95)
        unlockLocationButton.font = baseFont
        unlockLocationButton.addActionListener(this)
        add(unlockLocationButton)

        //---REPLAY BUTTON ---------------------------------------------------------------------------------------------
        replayButton = JButton("Play Again!")
        replayButton.bounds = Rectangle(555, 445, 143, 95)
        replayButton.font = baseFont
        replayButton.addActionListener(this)
        replayButton.isVisible = false
        add(replayButton)





    }

    /**
     * Handle any UI events
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            goNorthButton -> goNorth()
            goSouthButton -> goSouth()
            goEastButton -> goEast()
            goWestButton -> goWest()
            unlockLocationButton -> unlockLocation()
            replayButton -> replay()
        }
    }

    private fun showLocation() {
        // Check if movement options are valid, update text within movement buttons
        updateMovementButtons()

        // Show unlock location button if location is locked
        if (!currentLocation.canGoPast) {
            unlockLocationButton.isVisible = true
            unlockLocationButton.text = "<html>Use ${currentLocation.unlockedBy!!.name}"

            // Allow use of unlock button if player has required item
            if (inventory.any{it == currentLocation.unlockedBy}) {
                unlockLocationButton.isEnabled = true
            }
        }
        else {
            // Hide unlock location button
            unlockLocationButton.isVisible = false
            unlockLocationButton.isEnabled = false
        }

        // Update display labels to show new location, description, image
        currentLocationLabel.text = currentLocation.name
        locationDescriptionLabel.text = "<html>" + currentLocation.description
        updateImageIcon()
        updateInventory()
        checkForWin()


    }
    private fun updateImageIcon() {
        var currentLocationImage =
            if (currentLocation.canGoPast) {
                // Show unlocked location image
            ImageIcon("${currentLocation.unlockedImageLocated}").image
            }
            else {
                // Show locked location image
                ImageIcon("${currentLocation.lockedImageLocated}").image
            }

        // Resize image and add to label
        currentLocationImage = currentLocationImage.getScaledInstance(250,250, Image.SCALE_SMOOTH)
        locationImageLabel.icon = ImageIcon(currentLocationImage)

    }

    private fun updateMovementButtons() {
        // Enable movement buttons if player has valid movement options
        goNorthButton.isEnabled = (currentLocation.north != null)
                && (currentLocation.canGoPast || currentLocation.north == previousLocation)
        goSouthButton.isEnabled = (currentLocation.south != null)
                && (currentLocation.canGoPast || currentLocation.south == previousLocation)
        goEastButton.isEnabled = (currentLocation.east != null)
                && (currentLocation.canGoPast || currentLocation.east == previousLocation)
        goWestButton.isEnabled = (currentLocation.west != null)
                && (currentLocation.canGoPast || currentLocation.west == previousLocation)


        // Update travel buttons to display name of locations connected to current location
        if (currentLocation.north != null) {
            goNorthButton.text = "<html><div style='text-align: center;'>North<br>" + currentLocation.north!!.name
        } else { goNorthButton.text = "<html><div style='text-align: center;'>North<br>Nothing</html>" }

        if (currentLocation.south != null) {
            goSouthButton.text = "<html><div style='text-align: center;'>South<br>" + currentLocation.south!!.name
        } else { goSouthButton.text = "<html><div style='text-align: center;'>South<br>Nothing</html>" }

        if (currentLocation.east != null) {
            goEastButton.text = "<html><div style='text-align: center;'>East<br>" + currentLocation.east!!.name
        } else { goEastButton.text = "<html><div style='text-align: center;'>East<br>Nothing</html>" }

        if (currentLocation.west != null) {
            goWestButton.text = "<html><div style='text-align: center;'>West<br>" + currentLocation.west!!.name
        } else { goWestButton.text = "<html><div style='text-align: center;'>West<br>Nothing" }
    }


    /**
     *  Movement Functions
     * Perform error check, if valid change location in {direction} to current location
     */


    private fun goNorth() {
        // Error check => update north to current location
        if (currentLocation.north != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.north!!
            showLocation()
        }
    }

    private fun goSouth() {
        // Error check => update south to current location
        if (currentLocation.south != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.south!!
            showLocation()
        }
    }

    private fun goEast() {
        // Error check => update east to current location
        if (currentLocation.east != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.east!!
            showLocation()
        }
    }

    private fun goWest() {
        // Error check => update west to current location
        if (currentLocation.west != null) {
            previousLocation = currentLocation
            currentLocation = currentLocation.west!!
            showLocation()
        }
    }


    /**
     * Inventory Functions
     * Display and update player inventory
     */


    private fun updateInventory() {
        // Check if location has item
        if (currentLocation.item != null) {
            // Add item to inventory
            inventory.add(currentLocation.item!!)
            // Display found item dialog
            foundItemDialog.showItem(currentLocation.item!!.name)
            foundItemDialog.isVisible = true
            // Remove item from location
            currentLocation.item = null


        }


        var textUpdate = "<html>"
        inventory.forEach() {
            // Check if we need to update inventory display text
            textUpdate += "<li>${it.name}</li><br>"
        }
        if (textUpdate != "<html>") {
            // Update inventory display text
            inventoryDisplayLabel.text = textUpdate
        }
        else {
            inventoryDisplayLabel.text = "  You have no items."
        }
    }

    private fun unlockLocation() {
        // If this location is locked...
        while (!currentLocation.canGoPast) {
            inventory.forEach() {
                // Check player inventory for right item
                if (it == currentLocation.unlockedBy) {
                    // Unlock location
                    inventory.remove(it)
                    currentLocation.unlockedBy = null
                    currentLocation.canGoPast = true


                    showLocation()
                    return
                }
            }
        }
    }


    /**
     * Win Functions
     * Display victory dialogue, allow replay
     */


    private fun checkForWin() {
        // Win if player at the exit
        if (currentLocation.name == "Freedom!!!") {
            winDialog
            winDialog.isVisible = true
            // Enable replay button
            replayButton.isVisible = true
            replayButton.isEnabled = true
            // Disable movement
            goNorthButton.isEnabled = false


        }
    }

    private fun replay() {
        locations.removeAll(locations)
        setupGame()
        currentLocation = locations.first()
        replayButton.isVisible = false
        showLocation()

    }
}

//==Dialogs=============================================================================================================


class FoundItemDialog() : JDialog() {
    private val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

    private lateinit var foundItemLabel: JLabel
    init {
        setupWindow()
        buildUI()
        setLocationRelativeTo(null)
    }

    private fun setupWindow() {
        title = "FOUND ITEM"
        contentPane.preferredSize = Dimension(200, 100)
        isResizable = false
        isModal = true
        layout = null
        pack()
    }

    private fun buildUI() {
        foundItemLabel = JLabel("FOUND ITEM", SwingConstants.CENTER)
        foundItemLabel.bounds = Rectangle(20,20,160,60)
        foundItemLabel.font = baseFont
        add(foundItemLabel)
    }





    fun showItem(itemName: String) {
        // Show what item player has found
        title = "You found $itemName!"
        foundItemLabel.text = "<html>You found $itemName!"
    }
}

//--Win Dialog----------------------------------------------------------------------------------------------------------

class WinDialog() : JDialog() {
    private val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

    private lateinit var winDialogLabel: JLabel
    init {
        setupWindow()
        buildUI()
        setLocationRelativeTo(null)
    }

    private fun setupWindow() {
        title = "You win!"
        contentPane.preferredSize = Dimension(300, 300)
        isResizable = false
        isModal = true
        layout = null
        pack()
    }


    private fun buildUI() {
        // Tell player they've won
        winDialogLabel = JLabel("YOU WIN", SwingConstants.CENTER)
        winDialogLabel.bounds = Rectangle(20,20,160,60)
        winDialogLabel.font = baseFont
        winDialogLabel.text = "<html>You found the exit! You win!"
        add(winDialogLabel)

    }

}
//======================================================================================================================

/**
 * Launch the application
 */
fun main() {
    // Flat, Dark look-and-feel
    FlatDarkLaf.setup()

    // Create the UI
    GUI()
}


