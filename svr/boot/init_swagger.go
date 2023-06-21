package boot

import (
	"github.com/gogf/gf/v2/frame/g"
	"github.com/gogf/swagger/v2"
)

/*
*

	初始化swagger
*/
func init() { 
	s := g.Server()
	s.Plugin(&swagger.Swagger{})
	s.SetPort(8199)
	s.Run()
}
