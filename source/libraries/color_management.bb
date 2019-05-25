; Module:		Color Library
; Purpose:		Provides easy means to manage colors
; Programmer:	Mario D. Flores
; Created:		Tue, May 21, 2019

;*****************************************************************
; NOTES

; Prefixes used
; All colors are prefixed with a two letter code.
; tc -> TriColor

; Useful Regexes
; /^.+(tc\_.+(?=\.)).+$(?# captures var name from declaration)/m
; 'SetTriColor\($1, 0, 0, 0\)' replacement string
;
; /.+(?=tc)(tc\_\w+).+(?# captures var name from assignment)/m
; 'Global $1\.TriColor \= New TriColor' replacement string

;*****************************************************************
; EXTERNAL LIBRAIRES


;*****************************************************************
; GLOBAL CONSTANTS


;*****************************************************************
; TYPES
Type TriColor
	Field r%
	Field g%
	Field b%
End Type

;*****************************************************************
; GLOBAL VARIABLES
; see notes for prefixes
Global tc_black.TriColor = New TriColor
Global tc_white.TriColor = New TriColor
Global tc_gray.TriColor = New TriColor

Global tc_red.TriColor = New TriColor
Global tc_green.TriColor = New TriColor
Global tc_blue.TriColor = New TriColor
Global tc_red_half.TriColor = New TriColor
Global tc_green_half.TriColor = New TriColor
Global tc_blue_half.TriColor = New TriColor

Global tc_cyan.TriColor = New TriColor
Global tc_magenta.TriColor = New TriColor
Global tc_yellow.TriColor = New TriColor
Global tc_cyan_half.TriColor = New TriColor
Global tc_magenta_half.TriColor = New TriColor
Global tc_yellow_half.TriColor = New TriColor

Global tc_orange.TriColor = New TriColor
Global tc_rose.TriColor = New TriColor
Global tc_chartreuse.TriColor = New TriColor
Global tc_spring.TriColor = New TriColor
Global tc_azure.TriColor = New TriColor
Global tc_violet.TriColor = New TriColor

;*****************************************************************
; FUNCTIONS
Function SetTriColor(tCol.TriColor, red%, green%, blue%)
	tCol\r = red
	tCol\g = green
	tCol\b = blue
End Function

Function SetEntityColor(hndEntity, newCol.TriColor)
	EntityColor hndEntity, newCol\r, newCol\g, newCol\b
End Function

Function GetColorMix.TriColor(srcColor.TriColor, targetColor.TriColor, transition#)
	retCol.TriColor = New TriColor
	retCol\r = srcColor\r - (srcColor\r - targetColor\r) * transition
	retCol\g = srcColor\g - (srcColor\g - targetColor\g) * transition
	retCol\b = srcColor\b - (srcColor\b - targetColor\b) * transition
	
	Return retCol
End Function

Function ParseColor(inputStr$)
	
End Function

;*****************************************************************
; INITIALIZATION
;BW
SetTriColor(tc_black, 0, 0, 0)
SetTriColor(tc_white, 255, 255, 255)
SetTriColor(tc_gray, 127, 127, 127)

;RGB
SetTriColor(tc_red, 255, 0, 0)
SetTriColor(tc_green, 0, 255, 0)
SetTriColor(tc_blue, 0, 0, 255)
SetTriColor(tc_red_half, 127, 0, 0)
SetTriColor(tc_green_half, 0, 127, 0)
SetTriColor(tc_blue_half, 0, 0, 127)

;CMY
SetTriColor(tc_cyan, 0, 255, 255)
SetTriColor(tc_magenta, 255, 0, 255)
SetTriColor(tc_yellow, 255, 255, 0)
SetTriColor(tc_cyan_half, 0, 127, 127)
SetTriColor(tc_magenta_half, 127, 0, 127)
SetTriColor(tc_yellow_half, 127, 127, 0)

;Full-Mid
SetTriColor(tc_orange, 255, 127, 0)
SetTriColor(tc_rose, 255, 0, 127)
SetTriColor(tc_chartreuse, 127, 255, 0)
SetTriColor(tc_spring, 0, 255, 127)
SetTriColor(tc_azure, 0, 127, 255)
SetTriColor(tc_violet, 127, 0, 255)

