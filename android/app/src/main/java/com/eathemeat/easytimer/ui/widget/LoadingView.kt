package com.eathemeat.easytimer.ui.widget

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import com.eathemeat.easytimer.R


class LoadingView : View {

    lateinit var mFixedBlocks:Array<FixedBlock>
    lateinit var mMoveBlock:MoveBlock
    lateinit var mPaint:Paint


    //方块属性
    var halfBlockWidth:Float =-1F
    var blockInterval:Float = -1F
    var isClockWise:Boolean =false
    var initPos:Int = 0
    var mCurEmptyPos:Int = 0
    var lineNum:Int = 0
    var blockColor:Int = Color.YELLOW

    //方块的圆角半径
    var moveBlockAngle:Float = 0F
    var fixBlockAngle:Float = 0F


    //动画属性
    var mRotateDegree:Float = 0F
    var mAllowRoll:Boolean = false
    var isMoving:Boolean =false
    var moveSpeed:Int = 250

    //动画插值器
    var moveInterpolator: Interpolator?=null

    constructor(ctx:Context) : this(ctx,null) {
    }

    constructor(ctx:Context,attrs:AttributeSet?):this(ctx,attrs,0) {
    }

    constructor(ctx:Context,attrs:AttributeSet?,defStyleAttr:Int):super(ctx,attrs,defStyleAttr) {
        initAttrs(ctx,attrs)
        init()
    }

    private fun init() {
        //初始化画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = blockColor

        //初始化方块
        initBlocks()
    }

    private fun initBlocks() {
        mFixedBlocks = Array(lineNum*lineNum) { i ->
            FixedBlock(RectF(), i, initPos == i, null)
        }
        mMoveBlock = MoveBlock(RectF(),0,false,0F,0F)
        relateOutBlock(mFixedBlocks,isClockWise)
    }

    private fun relateOutBlock(fixedBlock: Array<FixedBlock>, clockWise: Boolean) {
        //第一行
        for (i in 0 until lineNum) {
            when{
                i%lineNum == 0->{ //位于最左边
                     if (isClockWise) fixedBlock[i].next = fixedBlock[i+lineNum]
                    else fixedBlock[i].next = fixedBlock[i+1]
                }
                (i+1)%lineNum ==0->{  //位于最右边
                    if (isClockWise) fixedBlock[i].next = fixedBlock[i-1]
                    else fixedBlock[i].next = fixedBlock[i+lineNum]
                }
                else ->{
                    if (isClockWise) fixedBlock[i].next = fixedBlock[i-1]
                    else fixedBlock[i].next = fixedBlock[i+1]
                }
                }
            }
        //最后一行
        for (i in fixedBlock.size-lineNum until fixedBlock.size) {
            when{
                i%lineNum == 0->{ //位于最左边
                    if (isClockWise) fixedBlock[i].next = fixedBlock[i+1]
                    else fixedBlock[i].next = fixedBlock[i-lineNum]
                }
                (i+1)%lineNum ==0->{  //位于最右边
                    if (isClockWise) fixedBlock[i].next = fixedBlock[i-lineNum]
                    else fixedBlock[i].next = fixedBlock[i-1]
                }
                else ->{
                    if (isClockWise) fixedBlock[i].next = fixedBlock[i+1]
                    else fixedBlock[i].next = fixedBlock[i-1]
                }
            }
        }
        //第一列
        for (i in lineNum until fixedBlock.size step lineNum) {
            if (i == (lineNum-1)*lineNum) {
                if (isClockWise) {
                    fixedBlock[i].next = fixedBlock[i + 1]
                } else fixedBlock[i].next = fixedBlock[i - lineNum]
                continue
            }
            if (isClockWise) {
                fixedBlock[i].next = fixedBlock[i+lineNum]
            }else  fixedBlock[i].next = fixedBlock[i-lineNum]
        }
        //最后一列
        for (i in 2*lineNum-1 until fixedBlock.size step lineNum) {
            if (i == fixedBlock.size-1) {
                if (isClockWise) {
                    fixedBlock[i].next = fixedBlock[i - lineNum]
                } else fixedBlock[i].next = fixedBlock[i -1]
                continue
            }
            if (isClockWise) {
                fixedBlock[i].next = fixedBlock[i-lineNum]
            }else  fixedBlock[i].next = fixedBlock[i+lineNum]
        }
        }

    private fun initAttrs(ctx: Context, attrs: AttributeSet?) {
        // 控件资源名称
        // 控件资源名称
        val typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.loading_view_blocks)

        lineNum = Math.max(3,typedArray.getInteger(R.styleable.loading_view_blocks_lineNum,3))  // 方块行数量(最少3行)
        halfBlockWidth = typedArray.getDimension(R.styleable.loading_view_blocks_halfBlockWidth,
            30F
        )    // 半个方块的宽度（dp）
        blockInterval = typedArray.getDimension(R.styleable.loading_view_blocks_blockInterval, 10f)// 方块间隔宽度（dp）

        moveBlockAngle = typedArray.getFloat(R.styleable.loading_view_blocks_moveBlockAngle, 10f)// 移动方块的圆角半径

        fixBlockAngle = typedArray.getFloat(R.styleable.loading_view_blocks_fixBlockAngle, 30f)// 固定方块的圆角半径
        // 通过设置两个方块的圆角半径使得二者不同可以得到更好的动画效果哦

        // 方块颜色（使用十六进制代码，如#333、#8e8e8e）
        // 通过设置两个方块的圆角半径使得二者不同可以得到更好的动画效果哦
        blockColor = typedArray.getColor(R.styleable.loading_view_blocks_blockColor, blockColor) // 方块颜色（使用十六进制代码，如#333、#8e8e8e）


        initPos = typedArray.getInteger(R.styleable.loading_view_blocks_initPosition, 0) // 移动方块的初始位置（即空白位置）

        // 由于移动方块只能是外部方块，所以这里需要判断方块是否属于外部方块 -->关注1
        if (isInsideTheRect(initPos, lineNum)) {
            initPos = 0
        }
        // 动画方向是否 = 顺时针旋转
        isClockWise = typedArray.getBoolean(R.styleable.loading_view_blocks_isClockWise, true)

        // 移动方块的移动速度
        // 注：不建议使用者将速度调得过快
        // 因为会导致ValueAnimator动画对象频繁重复的创建，存在内存抖动
        moveSpeed = typedArray.getInteger(R.styleable.loading_view_blocks_moveSpeed, 150)

        // 设置移动方块动画的插值器
        val move_InterpolatorResId = typedArray.getResourceId(
            R.styleable.loading_view_blocks_moveInterpolator,
            android.R.anim.linear_interpolator
        )
        moveInterpolator = AnimationUtils.loadInterpolator(context, move_InterpolatorResId)

        // 当方块移动后，需要实时更新的空白方块的位置
        mCurEmptyPos = initPos

        // 释放资源
        typedArray.recycle()
    }

    //大小改变的时候设置初始位置
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w == oldw && h ==oldh) {
            return
        }
        var centerX = measuredWidth/2
        var centerY = measuredHeight/2
        fixedBlockPosition(mFixedBlocks,centerX,centerY,blockInterval,halfBlockWidth)

        MoveBlockPosition(mFixedBlocks,mMoveBlock,initPos,isClockWise)

    }

    override fun onDraw(canvas: Canvas?) {
        // 1. 绘制内部方块（固定的）
        canvas?.let {
            for (i in 0 until mFixedBlocks.size) {
                // 根据标志位判断是否需要绘制
                if (mFixedBlocks[i].needDraw) {
                    // 传入方块位置参数、圆角 & 画笔属性
                    canvas.drawRoundRect(
                        mFixedBlocks[i].rectF,
                        fixBlockAngle,
                        fixBlockAngle,
                        mPaint
                    )
                }
            }
            // 2. 绘制移动的方块（）
            if (mMoveBlock.needDraw) {
                canvas.rotate(
                    if (isClockWise) mRotateDegree else -mRotateDegree,
                    mMoveBlock.cx,
                    mMoveBlock.cy
                )
                canvas.drawRoundRect(mMoveBlock.rectF, moveBlockAngle, moveBlockAngle, mPaint)
            }
        }
        super.onDraw(canvas)
    }

    /**
     * 步骤5：启动动画
     */
    fun startMoving() {
        // 1. 根据标志位 & 视图是否可见确定是否需要启动动画
        // 此处设置是为了方便手动 & 自动停止动画
        if (isMoving || visibility != VISIBLE) {
            return
        }

        // 设置标记位：以便是否停止动画
        isMoving = true
        mAllowRoll = true

        // 2. 获取固定方块当前的空位置，即移动方块当前位置
        val currEmptyfixedBlock = mFixedBlocks[mCurEmptyPos]
        // 3. 获取移动方块的到达位置，即固定方块当前空位置的下1个位置
        val movedBlock: FixedBlock = currEmptyfixedBlock.next!!

        // 4. 设置方块动画 = 移动方块平移 + 旋转
        // 原理：设置平移动画（Translate） + 旋转动画（Rotate），最终通过组合动画（AnimatorSet）组合起来

        // 4.1 设置平移动画：createTranslateValueAnimator（） ->>关注1
        // 平移路径 = 初始位置 - 到达位置
        val translateConrtroller: ValueAnimator = createTranslateValueAnimator(
            currEmptyfixedBlock,
            movedBlock
        )

        // 4.2 设置旋转动画：createMoveValueAnimator(（）->>关注3
        val moveConrtroller: ValueAnimator = createMoveValueAnimator()

        // 4.3 将两个动画组合起来
        // 设置移动的插值器
        var animatorSet = AnimatorSet()
        animatorSet.setInterpolator(moveInterpolator)
        animatorSet.playTogether(translateConrtroller, moveConrtroller)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            // 动画开始时进行一些设置
            override fun onAnimationStart(animation: Animator) {

                // 每次动画开始前都需要更新移动方块的位置 ->>关注4
                updateMoveBlock()

                // 让移动方块的初始位置的下个位置也隐藏 = 两个隐藏的方块
                mFixedBlocks[mCurEmptyPos].next!!.needDraw = false

                // 通过标志位将移动的方块显示出来
                mMoveBlock.needDraw = true
            }

            // 结束时进行一些设置
            override fun onAnimationEnd(animation: Animator) {
                isMoving = false
                mFixedBlocks[mCurEmptyPos].needDraw = true
                mCurEmptyPos = mFixedBlocks[mCurEmptyPos].next!!.index

                // 将移动的方块隐藏
                mMoveBlock.needDraw = false

                // 通过标志位判断动画是否要循环播放
                if (mAllowRoll) {
                    startMoving()
                }
            }
        })

        // 启动动画
        animatorSet.start()
    }

    /**
     * 关注1：设置平移动画
     */
    private fun createTranslateValueAnimator(
        currEmptyfixedBlock: FixedBlock,
        moveBlock: FixedBlock
    ): ValueAnimator {
        var startAnimValue = 0f
        var endAnimValue = 0f
        var left: PropertyValuesHolder? = null
        var top: PropertyValuesHolder? = null

        // 1. 设置移动速度
        val valueAnimator = ValueAnimator().setDuration(moveSpeed.toLong())


        // 2. 设置移动方向
        // 情况分为：4种，分别是移动方块向左、右移动 和 上、下移动
        // 注：需考虑 旋转方向（isClockWise），即顺逆时针 ->>关注1.1
        if (isNextRollLeftOrRight(currEmptyfixedBlock, moveBlock)) {

            // 情况1：顺时针且在第一行 / 逆时针且在最后一行时，移动方块向右移动
            if (isClockWise && currEmptyfixedBlock.index > moveBlock.index || !isClockWise && currEmptyfixedBlock.index > moveBlock.index) {
                startAnimValue = moveBlock.rectF.left
                endAnimValue = moveBlock.rectF.left + blockInterval

                // 情况2：顺时针且在最后一行 / 逆时针且在第一行，移动方块向左移动
            } else if (isClockWise && currEmptyfixedBlock.index < moveBlock.index
                || !isClockWise && currEmptyfixedBlock.index < moveBlock.index
            ) {
                startAnimValue = moveBlock.rectF.left
                endAnimValue = moveBlock.rectF.left - blockInterval
            }

            // 设置属性值
            left = PropertyValuesHolder.ofFloat("left", startAnimValue, endAnimValue)
            valueAnimator.setValues(left)
        } else {
            // 情况3：顺时针且在最左列 / 逆时针且在最右列，移动方块向上移动
            if (isClockWise && currEmptyfixedBlock.index < moveBlock.index
                || !isClockWise && currEmptyfixedBlock.index < moveBlock.index
            ) {
                startAnimValue = moveBlock.rectF.top
                endAnimValue = moveBlock.rectF.top - blockInterval

                // 情况4：顺时针且在最右列 / 逆时针且在最左列，移动方块向下移动
            } else if (isClockWise && currEmptyfixedBlock.index > moveBlock.index
                || !isClockWise && currEmptyfixedBlock.index > moveBlock.index
            ) {
                startAnimValue = moveBlock.rectF.top
                endAnimValue = moveBlock.rectF.top + blockInterval
            }

            // 设置属性值
            top = PropertyValuesHolder.ofFloat("top", startAnimValue, endAnimValue)
            valueAnimator.setValues(top)
        }

        // 3. 通过监听器更新属性值
        valueAnimator.addUpdateListener { animation ->
            val left = animation.getAnimatedValue("left")
            val top = animation.getAnimatedValue("top")
            if (left != null) {
                mMoveBlock.rectF.offsetTo((left as Float), mMoveBlock.rectF.top)
            }
            if (top != null) {
                mMoveBlock.rectF.offsetTo(mMoveBlock.rectF.left, (top as Float))
            }
            // 实时更新旋转中心 ->>关注2
            setMoveBlockRotateCenter(mMoveBlock, isClockWise)

            // 更新绘制
            invalidate()
        }
        return valueAnimator
    }
    // 回到原处


    // 回到原处
    /**
     * 关注1.1：判断移动方向
     * 即上下 or 左右
     */
    private fun isNextRollLeftOrRight(
        currEmptyfixedBlock: FixedBlock,
        rollSquare: FixedBlock
    ): Boolean {
        return (currEmptyfixedBlock.rectF.left - rollSquare.rectF.left) != 0F
    }

    /**
     * 关注2：实时更新移动方块的旋转中心
     * 因为方块在平移旋转过程中，旋转中心也会跟着改变，因此需要改变MoveBlock的旋转中心（cx,cy）
     */
    private fun setMoveBlockRotateCenter(moveBlock: MoveBlock, isClockwise: Boolean) {

        // 情况1：以移动方块的左上角为旋转中心
        if (moveBlock.index == 0) {
            moveBlock.cx = moveBlock.rectF.right
            moveBlock.cy = moveBlock.rectF.bottom

            // 情况2：以移动方块的右下角为旋转中心
        } else if (moveBlock.index == lineNum * lineNum - 1) {
            moveBlock.cx = moveBlock.rectF.left
            moveBlock.cy = moveBlock.rectF.top

            // 情况3：以移动方块的左下角为旋转中心
        } else if (moveBlock.index == lineNum * (lineNum - 1)) {
            moveBlock.cx = moveBlock.rectF.right
            moveBlock.cy = moveBlock.rectF.top

            // 情况4：以移动方块的右上角为旋转中心
        } else if (moveBlock.index == lineNum - 1) {
            moveBlock.cx = moveBlock.rectF.left
            moveBlock.cy = moveBlock.rectF.bottom
        } else if (moveBlock.index % lineNum === 0) {
            moveBlock.cx = moveBlock.rectF.right
            moveBlock.cy = if (isClockwise) moveBlock.rectF.top else moveBlock.rectF.bottom

            // 情况2：上边
        } else if (moveBlock.index < lineNum) {
            moveBlock.cx = if (isClockwise) moveBlock.rectF.right else moveBlock.rectF.left
            moveBlock.cy = moveBlock.rectF.bottom

            // 情况3：右边
        } else if ((moveBlock.index + 1) % lineNum === 0) {
            moveBlock.cx = moveBlock.rectF.left
            moveBlock.cy = if (isClockwise) moveBlock.rectF.bottom else moveBlock.rectF.top

            // 情况4：下边
        } else if (moveBlock.index > (lineNum - 1) * lineNum) {
            moveBlock.cx = if (isClockwise) moveBlock.rectF.left else moveBlock.rectF.right
            moveBlock.cy = moveBlock.rectF.top
        }
    }


    /**
     * 关注3：设置旋转动画
     */
    private fun createMoveValueAnimator(): ValueAnimator {

        // 通过属性动画进行设置
        val moveAnim = ValueAnimator.ofFloat(0f, 90f).setDuration(moveSpeed.toLong())
        moveAnim.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue

            // 赋值
            mRotateDegree = animatedValue as Float

            // 更新视图
            invalidate()
        }
        return moveAnim
    }

    /**
     * 关注4：更新移动方块的位置
     */
    private fun updateMoveBlock() {
        mMoveBlock.rectF.set(mFixedBlocks[mCurEmptyPos].next!!.rectF)
        mMoveBlock.index = mFixedBlocks[mCurEmptyPos].next!!.index
        setMoveBlockRotateCenter(mMoveBlock, isClockWise)
    }
    // 回到原处


    // 回到原处
    /**
     * 停止动画
     */
    fun stopMoving() {

        // 通过标记位来设置
        mAllowRoll = false
    }




    /**
     * 关注2：设置移动方块的位置
     */
    private fun MoveBlockPosition(fixedBlocks: Array<LoadingView.FixedBlock>, moveBlock: LoadingView.MoveBlock, initPos: Int, clockWise: Boolean) {
        // 移动方块位置 = 设置初始的空出位置 的下一个位置（next）
        // 下一个位置 通过 连接的外部方块位置确定
        val fixedBlock: FixedBlock = fixedBlocks[initPos]
        moveBlock!!.rectF.set(fixedBlock.next!!.rectF)
    }

    /**
     * 设置固定方块位置
     */
    private fun fixedBlockPosition(
        fixedBlocks: Array<FixedBlock>,
        centerX: Int,
        centerY: Int,
        dividerWidth: Float,
        halfSquareWidth: Float
    ) {
         var squareWidth = halfBlockWidth*2
        var firstRectLeft:Float = 0F
        var firstRectTop:Float = 0F
        when{
            fixedBlocks.size%2 == 0->{
                var squareCountInline = lineNum/2
                var divCountInline = squareCountInline-1
                var firstRectLeftTopFromCenter = squareCountInline*squareWidth+divCountInline*dividerWidth+dividerWidth/2
                firstRectLeft = centerX-firstRectLeftTopFromCenter
                firstRectTop = centerY-firstRectLeftTopFromCenter
            }
            else ->{
                var squareCountInline = lineNum/2
                var divCountInline = squareCountInline
                val firstRectLeftTopFromCenter: Float =
                    squareCountInline * squareWidth + divCountInline * dividerWidth + halfSquareWidth
                firstRectLeft = centerX - firstRectLeftTopFromCenter
                firstRectTop = centerY - firstRectLeftTopFromCenter
            }
        }

        for (i in 0 until lineNum) {
            for (j in 0 until lineNum) {
                if (i ==0) {
                    if (j == 0) {
                        fixedBlocks[i].rectF.set(firstRectLeft,firstRectTop,firstRectLeft+squareWidth,firstRectTop+squareWidth)
                    }else{
                        val currIndex: Int = i * lineNum + j
                        fixedBlocks[currIndex].rectF.set(fixedBlocks[currIndex - 1].rectF)
                        fixedBlocks[currIndex].rectF.offset(dividerWidth + squareWidth, 0F)
                    }
                }else {
                    val currIndex: Int = i * lineNum + j
                    fixedBlocks[currIndex].rectF.set(fixedBlocks[currIndex - lineNum].rectF)
                    fixedBlocks[currIndex].rectF.offset(0F, dividerWidth + squareWidth)
                }
            }
        }

    }

    /**
     * 关注1：判断方块是否在内部
     */
    private fun isInsideTheRect(pos: Int, lineCount: Int): Boolean {
        // 判断方块是否在第1行
        if (pos < lineCount) {
            return false
            // 是否在最后1行
        } else if (pos > lineCount * lineCount - 1 - lineCount) {
            return false
            // 是否在最后1行
        } else if ((pos + 1) % lineCount == 0) {
            return false
            // 是否在第1行
        } else if (pos % lineCount == 0) {
            return false
        }
        // 若不在4边，则在内部
        return true
    }


    /**
     *
     *  方块的基本属性
     * @param rectF 方块的位置信息
     * @param index 方块的下标
     * @param needDraw 是否需要重新绘制
     */
    open class Block(var rectF:RectF, var index:Int, var needDraw:Boolean)

    /**
     * 固定的方块
     * * @param 下一个需要移动的位置
     */
    class FixedBlock(rectF:RectF, index:Int, needDraw:Boolean, var next: FixedBlock?):Block(rectF,index,needDraw)

    /**
     * 移动的方块
     * @param cx cy  移动的中心坐标
     */
    class MoveBlock(rectF:RectF, index:Int, needDraw:Boolean,var cx:Float,var cy:Float):Block(rectF,index,needDraw)


}