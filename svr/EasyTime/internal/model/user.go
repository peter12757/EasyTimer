package model

type UserCreateInput struct {
	Passport string
	Password string
	Phone string
	NickName string
}


type UserSignInput struct {
	Passport string
	Password string
}