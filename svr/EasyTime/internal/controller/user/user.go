package user

import (
	v1 "EasyTime/api/usr/v1"
	"EasyTime/internal/model"
	"EasyTime/internal/service"
	"context"
)

type Controller struct{}

func New() *Controller {
	return &Controller{}
}

func (c *Controller) SignUp(ctx context.Context, req *v1.SignUpReq) (res *v1.SignUpRes, err error) {
	err = service.User().Create(ctx, model.UserCreateInput{
		Passport: req.Passport,
		Password: req.Password,
		NickName: req.NickName,
		Phone:    req.Phone,
	})
	return
}

func (c *Controller) SignIn(ctx context.Context, req *v1.SignInReq) (res *v1.SignInRes, err error) {
	err = service.User().SignIn(ctx, model.UserSignInput{
		Passport:  req.Passport, 
		Password:  req.Password,
		LoginType: req.LoginType,
	})
	return
}
