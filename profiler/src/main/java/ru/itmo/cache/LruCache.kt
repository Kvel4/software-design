package ru.itmo.cache


open class LruCache<K, V>(val capacity: Int) {
    private val list = DoubleLinkedList<Pair<K, V>>()
    private val map = HashMap<K, Node<Pair<K, V>>>(capacity)

    init {
        assert(capacity >= 1)
    }

    open fun get(key: K): V? {
        assert(map.size == list.size)
        return map[key]?.value?.second
    }

    open fun put(key: K, value: V) {
        assert(map.size == list.size)
        val node = map[key]

        if (node != null) {
            list.remove(node)
            list.addFirst(node)
        } else {
            if (map.size == capacity) {
                map.remove(list.removeLast()!!.value.first)
            }
            val newNode = Node(Pair(key, value))
            list.addFirst(newNode)
            map[key] = newNode
        }

        assert(map.size == list.size)
        assert(list.size <= capacity)
    }

    open fun test() = test1() + test1() + test1() + test1()

    open fun test1() = 1

    private inner class DoubleLinkedList<T> {
        private var head: Node<T>? = null
        private var tail: Node<T>? = null
        var size = 0
            private set

        fun addFirst(node: Node<T>): Node<T> {
            val prevHead = head
            head = node
            if (prevHead == null) {
                tail = node
            } else {
                prevHead.prev = node
            }
            size++

            return node
        }

        fun removeLast() = tail?.let { remove(it) }

        fun remove(curr: Node<T>): Node<T> {
            val next = curr.next
            val prev = curr.prev

            if (prev == null) {
                head = next
            } else {
                prev.next = next
                curr.prev = null
            }

            if (next == null) {
                tail = prev
            } else {
                next.prev = prev
                curr.next = null
            }
            size--

            assert(size >= 0)
            return curr
        }
    }

    private data class Node<T> constructor(var value: T, var prev: Node<T>? = null, var next: Node<T>? = null)
}



