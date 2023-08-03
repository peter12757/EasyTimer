package user

import (
	v1 "ProjectRoot/api/usr/v1"
	"ProjectRoot/internal/model"
	"ProjectRoot/internal/service"
	"context"
)

type Controller struct{}

func New() *Controller {
	return &Controller{}
}

func (c *Controller) Register(ctx context.Context, req *v1.RegisterReq) (res *v1.RegisterRes, err error) {
	err = service.User().Register(ctx, model.UserRegisterInput{
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
