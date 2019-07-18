; Program:		Collisions Example
; Objective:	Modify example provided in Blitz documentation
; Programmer:	Mario D. Flores
; Date:			Tue, May 21, 2019

;*****************************************************************
; EXTERNAL LIBRAIRES
; custom library for colors
Include "libraries\color_management.bb"

;*****************************************************************
; GLOBAL CONSTANTS
;View-port dimensions
Const VP_WIDTH = 1280
Const VP_HEIGHT = 720

;Color modes
Const CM_BIT_DEPTH = 32

;Graphic modes
Const GM_AUTO_MODE = 0
Const GM_FULLSCREEN_MODE = 1
Const GM_WINDOWED_MODE = 2
Const GM_SCALED_WINDOWED_MODE = 3

;Camera Modes
Const CM_FOG_DISABLED = 0
Const CM_FOG_ENABLED = 1

;Light Modes
Const LM_DIRECTIONAL = 1
Const LM_POINT = 2
Const LM_SPOT = 3

;Scan Codes
Const SC_ESQ = 1
Const SC_SPACE = 57
Const SC_KEY_R = 19
Const SC_KEY_M = 50
Const SC_UP = 200
Const SC_LEFT = 203
Const SC_RIGHT = 205
Const SC_DOWN = 208
Const SC_KEY_W = 17 ;Up
Const SC_KEY_A = 30 ;Left
Const SC_KEY_S = 31 ;Down
Const SC_KEY_D = 32 ;Right

;Collision type values
;These are arbitrary collision domains. Used with Collisions
Const COLLIDE_GROUND = 1
Const COLLIDE_CHARACTER = 2
Const COLLIDE_SCENERY = 3
Const COLLIDE_GOAL = 4

Const FRAMERATE = 60

Const GRAVITY_ACCEL# = -2*0.01 ;was 8;was -0.023
Const MOVE_FORCE# = 4.75 * 0.02 ;was 9.5;was 0.1
Const JUMP_FORCE# = 20 * 0.02 ;was 40;was 0.45

Const FALL_DAMAGE# = -0.6 ;was -1.20 ;was 0.8
Const GRACE_PERIOD = 60

;*****************************************************************
; TYPES
;Nothing here yet

;*****************************************************************
; GLOBAL VARIABLES

;The following 4 variables define the boundaries for the player
Global Xbound_p = 20
Global Xbound_n = -20
Global Zbound_p = 20
Global Zbound_n = -20

;*****************************************************************
; FUNCTIONS
;Nothing here yet

;*****************************************************************
; MAIN

; NOTE: this is a modified version of the example code that is
; provided in Blitz3D under Command Reference in the help page for
; the collisions function.
; For some reason whoever made this code originally decided that
; the Y axis is up and the horizontal axes are X and Z. I've
; kept it this way. However, be aware that the vast majority of 3D
; applications and video games usually have Z as the vertical axis
; and X and Y as the horizontal axes.

;Setup graphics mode
Graphics3D VP_WIDTH, VP_HEIGHT, CM_BIT_DEPTH, GM_WINDOWED_MODE
SetBuffer BackBuffer()

; This uses my custom library to declare colors
darkSpring.TriColor = GetColorMix(tc_spring, tc_black, 0.45)
darkAzure.TriColor = GetColorMix(tc_azure, tc_black, 0.45)

; Set collision method and response values
method = 2 ;ellipsoid to polygon
response = 2 ;full sliding collision

; Create camera
camera=CreateCamera()
RotateEntity camera,45,0,0
PositionEntity camera,0,15,-10
CameraZoom camera, 1.25
microphone = CreateListener(camera)
;CameraFogMode camera, CM_FOG_ENABLED
;CameraFogRange camera, 18, 30

; Create cube 'ground'
cube=CreateCube()
ScaleEntity cube,20,10,20
EntityAlpha cube, 0
;SetEntityColor(cube, darkSpring)
EntityType cube,COLLIDE_GROUND
PositionEntity cube,0,-5,0

; Create cube 'ground'
ground = CreatePlane(10)
SetEntityColor(ground, darkSpring)
PositionEntity ground,0,-5,0

; Create sphere 'character'
sphere=CreateSphere( 32 )
SetEntityColor(sphere, tc_gray)
EntityRadius sphere,1
EntityType sphere,COLLIDE_CHARACTER
PositionEntity sphere,0,7,0

; Create light
light=CreateLight(LM_SPOT, sphere)
PositionEntity light, 0, 11, 0
PointEntity light, sphere
LightColor light, 7, 7, 7
AmbientLight 5, 5, 5

; Enable collisions between COLLIDE_CHARACTER and COLLIDE_GROUND
Collisions COLLIDE_CHARACTER,COLLIDE_GROUND, method, response

; Create cylinder 'scenery'
cylinder=CreateCylinder( 32 )
ScaleEntity cylinder,2,2,2
SetEntityColor(cylinder, darkAzure)
EntityRadius cylinder,2
EntityBox cylinder,-2,-2,-2,4,4,4
EntityType cylinder,COLLIDE_SCENERY
PositionEntity cylinder,-4,7,-4

; Create prism 'scenery'
prism=CreateCylinder( 3 )
ScaleEntity prism,2,2,2
SetEntityColor(prism, darkAzure)
EntityRadius prism,2
EntityBox prism,-2,-2,-2,4,4,4
EntityType prism,COLLIDE_SCENERY
PositionEntity prism,-4,11,4
RotateEntity prism,0,180,0

; Create pyramid 'scenery'
pyramid=CreateCone( 8 )
ScaleEntity pyramid,2,2,2
SetEntityColor(pyramid, darkAzure)
EntityRadius pyramid,2
EntityBox pyramid,-2,-2,-2,4,4,4
EntityType pyramid,COLLIDE_SCENERY
RotateEntity pyramid,0,45,0
PositionEntity pyramid,4,17,4

; Create pyramid 'scenery'
cone=CreateCone( 8 )
ScaleEntity cone,1,1,1
SetEntityColor(cone, darkAzure)
EntityRadius cone,2
EntityBox cone,-1,-1,-1,2,2,2
EntityType cone,COLLIDE_SCENERY
PositionEntity cone,4,22,-4

; Create pyramid 'scenery'
cone=CreateSphere( 4 )
ScaleEntity cone, 0.25, 0.25, 0.25
SetEntityColor(cone, darkAzure)
EntityRadius cone,0.25
;EntityBox cone, -0.5, -0.5, -0.5, 1, 1, 1
EntityType cone,COLLIDE_SCENERY
PositionEntity cone,-5.5,22,-4

; Create pyramid 'scenery'
goal=CreateSphere( 8 )
ScaleEntity goal,1,1,1
SetEntityColor(goal, tc_yellow_half)
EntityRadius goal,1
;EntityBox goal,-1,-1,-1,2,2,2
EntityType goal,COLLIDE_GOAL
PositionEntity goal,-13,25,-4

Collisions COLLIDE_CHARACTER,COLLIDE_GOAL, method, response

;will store sphere speed
speed_x# = 0
speed_y# = 0
speed_z# = 0

;will store sphere coordinates
sphereX# = 0
sphereY# = 0
sphereZ# = 0

;detects if collision detection needs to be bypassed
collisionFlag = True
;counts the times jumped before landing (to allow only a double jump)
jumpCount = 0
;sets to true if the player won
winflag = False
;ground level flag
isFalling = True

;These variables control the frame rate
frameDuration = 0
last_timer = MilliSecs()
DesiredDelay# = 1000.0 / FRAMERATE

;stores frame delay for display purposes
dispDelay# = 0

;loads sounds
fallSound = Load3DSound("sound\rattle_knock.wav")
jumpSound = Load3DSound("sound\nosie_fadeout.wav")

;time between collisions
collDelay = 0
lastColl = MilliSecs()

;health of the sphere
charHealth# = 100.0

;game loop starts here
While (Not KeyDown(SC_ESQ)) And winflag = false And charHealth > 0
	
	;updates timer
	last_timer = MilliSecs()
	;frame rate control
	If frameDuration > DesiredDelay Then
		dispDelay = frameDuration ;1000 / frameDuration
	Else
		delay DesiredDelay - frameDuration
		dispDelay = DesiredDelay
	End if
	
	speed_x#=0
	speed_z#=0
	
	; Detect keyboard input to move sphere
	If KeyDown(SC_KEY_A) Then speed_x# = -MOVE_FORCE
	If KeyDown(SC_KEY_D) Then speed_x# = MOVE_FORCE
	If KeyDown(SC_KEY_S) Then speed_z# = -MOVE_FORCE
	If KeyDown(SC_KEY_W) Then speed_z# = MOVE_FORCE
	
	;if sphere collides with something reduces vertical speed to 0
	If EntityCollided(sphere, COLLIDE_GROUND) Or EntityCollided(sphere, COLLIDE_SCENERY) Then
		;sphere is on top of something
		
		;uses falling condition to make a sound on impact
		If isFalling And lastColl > GRACE_PERIOD Then
			EmitSound(fallSound, sphere)
		End If
		
		;record fastest collision while falling
		If speed_y < FALL_DAMAGE Then
			charHealth = charHealth + (speed_y - FALL_DAMAGE) * 350
		End If
		;reset jump
		jumpCount = 0
		;set the normal force
		speed_y = GRAVITY_ACCEL
		
		;update falling condition
		isFalling = False
		lastColl = MilliSecs()
	Else
		;sphere is free falling
		
		;accelerate object with gravity
		speed_y = speed_y + GRAVITY_ACCEL
		;update collision delay
		collDelay = MilliSecs() - lastColl
	End If
	
	;makes the sphere jump when pressing space bar
	If KeyHit(SC_SPACE) And jumpCount < 2 Then
		jumpCount = jumpCount + 1
		speed_y = JUMP_FORCE / (jumpCount ^ 0.5)
		isFalling = True
		EmitSound(jumpSound, sphere)
	End If
	
	;updates sphere location
	MoveEntity sphere, speed_x#, speed_y#, speed_z#
	
	;The following 4 if-statements allow the sphere to warp to the
	;opposite side of the plane when reaching the edge. It also sets
	;flag to bypass collision detection during current iteration to
	;keep sphere from getting stuck in the scenery.
	If sphereX > Xbound_p Then
		collisionFlag = False
		PositionEntity sphere, Xbound_n, sphereY, sphereZ
	End If
	If sphereX < Xbound_n Then
		collisionFlag = False
		PositionEntity sphere, Xbound_p, sphereY, sphereZ
	End If
	If sphereZ > Zbound_p Then
		collisionFlag = False
		PositionEntity sphere, sphereX, sphereY, Zbound_n
	End If
	If sphereZ < Zbound_n Then
		collisionFlag = False
		PositionEntity sphere, sphereX, sphereY, Zbound_p
	End If
	
	;gets sphere location
	sphereX = EntityX(sphere)
	sphereY = EntityY(sphere)
	sphereZ = EntityZ(sphere)
	
	;rearranges camera at a constant distance from the sphere
	PositionEntity camera, sphereX, sphereY + 6, sphereZ - 12
	;Points the camera at the sphere
	PointEntity camera, sphere
	
	;dynamically assigns a color to the sphere based on the
	;collision domain which is currently active. In other
	;words colors the sphere the same as whatever it is
	;colliding with.
	If EntityCollided(sphere, COLLIDE_SCENERY) Then
		SetEntityColor(sphere, darkAzure)
	Else If EntityCollided(sphere, COLLIDE_GROUND) Then
		SetEntityColor(sphere, darkSpring)
	Else If EntityCollided(sphere, COLLIDE_GOAL) Then
		winflag = True
	Else
		SetEntityColor(sphere, tc_gray)
	End If
	
	If (collisionFlag) Then
		;When true collision occurs normally
		;Enable collisions between COLLIDE_CHARACTER and COLLIDE_SCENERY
		Collisions COLLIDE_CHARACTER,COLLIDE_SCENERY, method, response
	Else
		;When false all collisions are disabled
		ClearCollisions
		; Re-Enable collisions between COLLIDE_CHARACTER and COLLIDE_GROUND
		Collisions COLLIDE_CHARACTER, COLLIDE_GROUND, method, response
		
		;set to true to re-enable collisions with scenery on next iteration
		collisionFlag = True
	End If
	
	; Perform collision checking
	UpdateWorld
	
	; Draws graphics to back buffer
	RenderWorld
	
	;draws HUD
	Text 0,  0, "Use WASD to move sphere. Press space-bar to jump or twice to double jump"
	Text 0, 20, "Find the golden sphere to win. Be careful falls are deadly!"
	Text 0, 40, "FPS: " + (1000 / dispDelay)
	Text 0, 60, "Health: " + charHealth
	;use this part of the code to analyze any variables you are curious about
	
	; Brings back buffer to front
	Flip
	;last frame duration
	frameDuration = MilliSecs() - last_timer
;game loop ends help
Wend

If (winflag) Then
	Text VP_WIDTH / 2, VP_HEIGHT / 2, "Congratulations You've Won!", True, True
	Flip
	Delay(1000)
Else If charHealth < 0 Then
	Text VP_WIDTH / 2, VP_HEIGHT / 2, "You Fell From Too High. Game Over!", True, True
	Flip
	Delay(1000)
Else
	;nothing to do
End If

;exit program
End
;*****************************************************************