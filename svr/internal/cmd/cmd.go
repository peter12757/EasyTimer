package cmd

import (
	"ProjectRoot/internal/controller/user"
	"ProjectRoot/internal/service"
	"context"

	"github.com/gogf/gf/v2/frame/g"
	"github.com/gogf/gf/v2/net/ghttp"
	"github.com/gogf/gf/v2/os/gcmd"
)

var (
	Main = gcmd.Command{
		Name:  "main",
		Usage: "main",
		Brief: "start http server",
		Func: func(ctx context.Context, parser *gcmd.Parser) (err error) {
			s := g.Server()
			s.Use(ghttp.MiddlewareHandlerResponse)
			s.Group("/", func(group *ghttp.RouterGroup) {
				group.Middleware(ghttp.MiddlewareHandlerResponse)

				//user
				group.Middleware(service.Middleware().Auth, ghttp.MiddlewareCORS)
				group.Bind(user.New())
			})
			s.Run()
			return nil
		},
	}
)
