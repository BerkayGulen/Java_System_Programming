.MODEL TINY
.CODE
.STARTUP
JMP START
NEW40 PROC FAR ;must be FAR
; your interrupt code goes in here
IRET ;must have an IRET
NEW40 ENDP
;start installation
START:
MOV AX,0 ;address segment 0000H
MOV DS,AX
MOV DS:[100H],OFFSET NEW40 ;save offset
MOV DS:[102H],CS ;save segment
MOV DX,OFFSET START
SHR DX,4
INC DX
MOV AX,3100H ;make NEW40 resident
INT 21H
END
